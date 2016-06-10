package com.doerksen.elasticsearch_service.resources.impl;

import com.doerksen.elasticsearch_service.resources.ElasticResource;
import com.doerksen.utilities.MessageFormatter;
import com.doerksen.utilities.Response;
import com.doerksen.utilities.ResponseImpl;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.io.stream.NotSerializableExceptionWrapper;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.mapper.MapperParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticResourceImpl implements ElasticResource {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticResourceImpl.class);

    private final Client esCluster;

    @Inject
    public ElasticResourceImpl(Client esCluster) {
        this.esCluster = esCluster;
    }

    public Response<String> getDocument(final String index,
                                        final String type,
                                        final String id) {
        Response<String> inputValidation = checkStringInputs(index, type, id);
        if (inputValidation.isError()) return inputValidation;
        inputValidation = checkNumericInputs(id);
        if (inputValidation.isError()) return inputValidation;

        try {
            return new ResponseImpl<>(esCluster.prepareGet(index, type, id).get().getSourceAsString());
        } catch (NullPointerException | IndexNotFoundException e) {
            return new ResponseImpl<>(MessageFormatter.format("No document found for index {}, type {}, and id {}.", index, type, id), HttpStatus.SC_NOT_FOUND, e);
        } catch (Exception e) {
            return responseBuilder(MessageFormatter.format("Error retrieving document at index {}, type {} and id {}.", index, type, id), e);
        }
    }

    public Response<String> postDocument(final String index,
                                         final String type,
                                         final String id,
                                         final String documentJSON) {
        Response<String> inputValidation = checkStringInputs(index, type, id, documentJSON);
        if (inputValidation.isError()) return inputValidation;
        inputValidation = checkNumericInputs(id);
        if (inputValidation.isError()) return inputValidation;

        try {
            Response<String> document = getDocument(index, type, id);
            // if we don't find it, create it, otherwise tell them to update it instead
            if (document.isError()) {
                esCluster.prepareIndex(index, type, id).setSource(documentJSON).get();
                return ResponseImpl.success(HttpStatus.SC_CREATED);
            } else {
                return new ResponseImpl<>(MessageFormatter.format("Unable to create document {} for index {}, type {} and id {} because the document already exists. Use put to update the document.", documentJSON, index, type, id), HttpStatus.SC_CONFLICT, false);
            }
        } catch (MapperParsingException e) {
            return new ResponseImpl<>(MessageFormatter.format("Error creating document {} for index {}, type {} and id {} because the JSON is malformed.", documentJSON, index, type, id), HttpStatus.SC_BAD_REQUEST, e);
        } catch (Exception e) {
            return responseBuilder(MessageFormatter.format("Error creating document {} for index {}, type {}, id {}.", documentJSON, index, type, id), e);
        }
    }

    /**
     * NOTE: Once a document exists, there is no check to ensure it is valid JSON when you update it
     * @param index
     * @param type
     * @param id
     * @param documentJSON
     * @return
     */
    public Response<String> putDocument(final String index,
                                        final String type,
                                        final String id,
                                        final String documentJSON) {
        Response<String> inputValidation = checkStringInputs(index, type, id, documentJSON);
        if (inputValidation.isError()) return inputValidation;
        inputValidation = checkNumericInputs(id);
        if (inputValidation.isError()) return inputValidation;

        try {
            Response<String> document = getDocument(index, type, id);
            // update if we find it, otherwise tell them to create it first
            if (document.isSuccess()) {
                esCluster.prepareUpdate(index, type, id)
                        .setDoc(documentJSON)
                        .get();
                return ResponseImpl.success(HttpStatus.SC_OK);
            } else {
                return new ResponseImpl<>(MessageFormatter.format("Unable to put document {} for index {}, type {} and id {} because the document does not exist. Post the document before attempting to update it.", documentJSON, index, type, id), HttpStatus.SC_NOT_FOUND, false);
            }
        } catch (NotSerializableExceptionWrapper e) {
            return new ResponseImpl<>(MessageFormatter.format("Error updating document {} for index {}, type {} and id {} because the JSON is malformed.", documentJSON, index, type, id), HttpStatus.SC_BAD_REQUEST, e);
        } catch (Exception e) {
            return responseBuilder(MessageFormatter.format("Error updating document {} for index {}, type {} and id {}.", documentJSON, index, type, id), e);
        }
    }

    public Response<String> deleteDocument(final String index,
                                           final String type,
                                           final String id) {
        Response<String> inputValidation = checkStringInputs(index, type, id);
        if (inputValidation.isError()) return inputValidation;
        inputValidation = checkNumericInputs(id);
        if (inputValidation.isError()) return inputValidation;

        try {
            DeleteResponse deleted = esCluster.prepareDelete(index, type, id).get();
            if (deleted.isFound()) {
                return ResponseImpl.success(HttpStatus.SC_OK);
            } else {
                return new ResponseImpl<>(MessageFormatter.format("Unable to delete document for index {}, type {} and id {} because it does not exist.", index, type, id), HttpStatus.SC_NOT_FOUND, false);
            }
        } catch (Exception e) {
            return responseBuilder(MessageFormatter.format("Error deleting document for index {}, type {} and id {}.", index, type, id), e);
        }
    }

    /**
     * Helper to run over all of our inputs to make sure they are valid
     * @param inputs
     */
    private Response<String> checkStringInputs(final String... inputs) {
        for (String input : inputs) {
            if (StringUtils.isBlank(input)) {
                return new ResponseImpl<>("One or more input parameters was null/empty.", HttpStatus.SC_BAD_REQUEST, false);
            }
        }
        return ResponseImpl.success(HttpStatus.SC_OK);
    }

    /**
     * Helper to run over all of our inputs to make sure they're valid
     * @param numbers
     */
    private Response<String> checkNumericInputs(final String... numbers) {
        for (String num : numbers) {
            if (Long.valueOf(num) < 0L) {
                return new ResponseImpl<>("One or more numbered values was < 0.", HttpStatus.SC_BAD_REQUEST, false);
            }
        }
        return ResponseImpl.success(HttpStatus.SC_OK);
    }

    /**
     * Builds and returns a response with the associated error message, exception thrown, and
     * modifies the status code to an appropriate one depending on the type of exception. By default,
     * it will set the status code to 500 unless a more appropriate one is found.
     *
     * e.g. 404 when a get request does not succeed because the requested index is not found.
     * @param errorMsg
     * @param e
     * @return
     */
    private Response<String> responseBuilder(final String errorMsg,
                                             final Exception e) {
        // set to warning because it's important that we send back a helpful status code to the caller
        LOG.warn("Unable to find a better status code for error message {}; defaulting to 500.", errorMsg, e);
        return new ResponseImpl<>(errorMsg, HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
    }
}
