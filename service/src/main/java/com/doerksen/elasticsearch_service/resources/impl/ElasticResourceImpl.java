package com.doerksen.elasticsearch_service.resources.impl;

import com.doerksen.elasticsearch_service.resources.ElasticResource;
import com.doerksen.utilities.MessageFormatter;
import com.doerksen.utilities.Response;
import com.doerksen.utilities.ResponseImpl;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.engine.VersionConflictEngineException;

public class ElasticResourceImpl implements ElasticResource {

    private static final Logger LOG = Logger.getLogger(ElasticResourceImpl.class);

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
        } catch (Exception e) {
            return responseBuilder(MessageFormatter.format("Unable to get document for index {}, type {} and id {}.", index, type, id), e);
        }
    }

    // TODO - status codes
    public Response<String> postDocument(final String index,
                                         final String type,
                                         final String id,
                                         final String documentJSON) {
        Response<String> inputValidation = checkStringInputs(index, type, id, documentJSON);
        if (inputValidation.isError()) return inputValidation;
        inputValidation = checkNumericInputs(id);
        if (inputValidation.isError()) return inputValidation;

        try {
            esCluster.prepareIndex(index, type, id).get();
            return ResponseImpl.success(HttpStatus.SC_CREATED);
        } catch (Exception e) {
            return responseBuilder(MessageFormatter.format("Unable to create document {} for index {}, type {}, id {}.", documentJSON, index, type, id), e);
        }
    }

    // TODO - status codes
    public Response<String> putDocument(final String index,
                                        final String type,
                                        final String id,
                                        final String documentJSON) {
        Response<String> inputValidation = checkStringInputs(index, type, id, documentJSON);
        if (inputValidation.isError()) return inputValidation;
        inputValidation = checkNumericInputs(id);
        if (inputValidation.isError()) return inputValidation;

        try {
            esCluster.prepareUpdate(index, type, id).get();
            return ResponseImpl.success(HttpStatus.SC_OK);
        } catch (Exception e) {
            return responseBuilder(MessageFormatter.format("Unable to update document {} for index {}, type {} and id {}.", documentJSON, index, type, id), e);
        }
    }

    // TODO - status codes
    public Response<String> deleteDocument(final String index,
                                           final String type,
                                           final String id) {
        Response<String> inputValidation = checkStringInputs(index, type, id);
        if (inputValidation.isError()) return inputValidation;
        inputValidation = checkNumericInputs(id);
        if (inputValidation.isError()) return inputValidation;

        try {
            esCluster.prepareDelete(index, type, id).get();
            return ResponseImpl.success(HttpStatus.SC_OK);
        } catch (Exception e) {
            return responseBuilder(MessageFormatter.format("Unable to delete document for index {}, type {} and id {}.", index, type, id), e);
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
        Response<String> response = new ResponseImpl<>(errorMsg, HttpStatus.SC_INTERNAL_SERVER_ERROR, e);

        if (e instanceof IndexNotFoundException) {
            response = new ResponseImpl<>(errorMsg, HttpStatus.SC_NOT_FOUND, e);
        } else if (e instanceof VersionConflictEngineException)

        if (response.getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            LOG.info("Unable to find a better status code for exception {}; defaulting to 500.", e);
        } else {
            String logLine = MessageFormatter.format("An error occurred while processing the request {}.", response.getMessage());
            LOG.debug(logLine, e);
        }

        return response;
    }
}
