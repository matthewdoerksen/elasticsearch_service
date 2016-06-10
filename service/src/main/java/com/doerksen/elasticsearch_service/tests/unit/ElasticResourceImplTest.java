package com.doerksen.elasticsearch_service.tests.unit;

import com.doerksen.elasticsearch_service.resources.ElasticResource;
import com.doerksen.utilities.MessageFormatter;
import com.doerksen.utilities.Response;
import com.doerksen.utilities.ResponseImpl;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ElasticResourceImplTest {

    /**
     * Only test the input/output of the resource here, for validating the responses
     * when the ES cluster fails and throws an exception, see the mocked ES client in
     * the ElasticResourceImplIntegTest class.
     */
    @Mock
    private ElasticResource elasticResource;

    @Before
    public void before() {
        elasticResource = mock(ElasticResource.class);
    }

    @Test()
    public void getDocumentExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("No document found for index {}, type {} and id {}.", "index", "type", "id");
        Response<String> getResponseWithErrorMsg = new ResponseImpl<>(errorMsg, HttpStatus.SC_NOT_FOUND, false);
        when(elasticResource.getDocument("index", "type", "id")).thenReturn(getResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.getDocument("index", "type", "id");
        assertEquals(getResponseWithErrorMsg.isError(), actualResponse.isError());
        assertTrue(getResponseWithErrorMsg.getObject().equals(actualResponse.getObject()));
        assertEquals(errorMsg, getResponseWithErrorMsg.getObject());
    }

    @Test
    public void postDocumentAlreadyExistsExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Unable to create document {} for index {], type {} and id {} because the document already exists. Use put to update the document.", "document", "index", "type", "id");
        Response<String> postResponseWithErrorMsg = new ResponseImpl<>(errorMsg, HttpStatus.SC_CONFLICT, false);
        when(elasticResource.postDocument("index", "type", "id", "document")).thenReturn(postResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.postDocument("index", "type", "id", "document");
        assertEquals(postResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(postResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
        assertEquals(errorMsg, postResponseWithErrorMsg.getObject());
    }

    @Test
    public void postDocumentExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Error creating document {} for index {], type {} and id {}.", "document", "index", "type", "id");
        Response<String> postResponseWithErrorMsg = new ResponseImpl<>(errorMsg, HttpStatus.SC_INTERNAL_SERVER_ERROR, false);
        when(elasticResource.postDocument("index", "type", "id", "document")).thenReturn(postResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.postDocument("index", "type", "id", "document");
        assertEquals(postResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(postResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
        assertEquals(errorMsg, postResponseWithErrorMsg.getObject());
    }

    @Test
    public void postDocumentBadJsonExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Error creating document {} for index {}, type {} and id {} because the JSON is malformed.", "document", "index", "type", "id");
        Response<String> postResponseWithErrorMsg = new ResponseImpl<>(errorMsg, HttpStatus.SC_BAD_REQUEST, false);
        when(elasticResource.postDocument("index", "type", "id", "document")).thenReturn(postResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.postDocument("index", "type", "id", "document");
        assertEquals(postResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(postResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
        assertEquals(errorMsg, postResponseWithErrorMsg.getObject());
    }

    @Test
    public void putDocumentDoesNotExistExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Unable to put document {} for index {}, type {} and id {} because the document does not exist. Post the document before attempting to update it.", "documentJSON", "index", "type", "id");
        Response<String> putResponseWithErrorMsg = new ResponseImpl<>(errorMsg, HttpStatus.SC_NOT_FOUND, false);
        when(elasticResource.putDocument("index", "type", "id", "document")).thenReturn(putResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.putDocument("index", "type", "id", "document");
        assertEquals(putResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(putResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
        assertEquals(errorMsg, putResponseWithErrorMsg.getObject());
    }

    @Test
    public void putDocumentBadJsonExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Error updating document {} for index {}, type {} and id {} because the JSON is malformed.", "document", "index", "type", "id");
        Response<String> postResponseWithErrorMsg = new ResponseImpl<>(errorMsg, HttpStatus.SC_BAD_REQUEST, false);
        when(elasticResource.postDocument("index", "type", "id", "document")).thenReturn(postResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.postDocument("index", "type", "id", "document");
        assertEquals(postResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(postResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
        assertEquals(errorMsg, postResponseWithErrorMsg.getObject());
    }

    @Test
    public void putDocumentExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Error updating document {} for index {}, type {} and id {}.", "document", "index", "type", "id");
        Response<String> putResponseWithErrorMsg = new ResponseImpl<>(errorMsg, HttpStatus.SC_INTERNAL_SERVER_ERROR, false);
        when(elasticResource.putDocument("index", "type", "id", "document")).thenReturn(putResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.putDocument("index", "type", "id", "document");
        assertEquals(putResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(putResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
        assertEquals(errorMsg, putResponseWithErrorMsg.getObject());
    }

    @Test
    public void deleteNonExistentDocumentExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Unable to delete document for index {}, type {} and id {} because it does not exist.", "index", "type", "id");
        Response<String> deleteResponseWithErrorMsg = new ResponseImpl<>(errorMsg, HttpStatus.SC_BAD_REQUEST, false);
        when(elasticResource.deleteDocument("index", "type", "id")).thenReturn(deleteResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.deleteDocument("index", "type", "id");
        assertEquals(deleteResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(deleteResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
    }

    @Test
    public void deleteDocumentExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Error deleting document for index {}, type {} and id {}.", "index", "type", "id");
        Response<String> deleteResponseWithErrorMsg = new ResponseImpl<>(errorMsg, HttpStatus.SC_INTERNAL_SERVER_ERROR, false);
        when(elasticResource.deleteDocument("index", "type", "id")).thenReturn(deleteResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.deleteDocument("index", "type", "id");
        assertEquals(deleteResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(deleteResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
    }

    @Test()
    public void getDocumentResponseTest() {
        Response<String> getResponse = new ResponseImpl<>("{json:\"what a json!\"}");
        when(elasticResource.getDocument("index", "type", "id")).thenReturn(getResponse);

        Response<String> actualResponse = elasticResource.getDocument("index", "type", "id");
        assertEquals(getResponse.isSuccess(), actualResponse.isSuccess());
        assertTrue(getResponse.getObject().equals(actualResponse.getObject()));
    }

    @Test
    public void postDocumentResponseTest() {
        Response<String> postResponse = ResponseImpl.success(200);
        when(elasticResource.postDocument("index", "type", "id", "{json:\"what a document!\"}")).thenReturn(postResponse);

        Response<String> actualResponse = elasticResource.postDocument("index", "type", "id", "{json:\"what a document!\"}");
        assertEquals(postResponse.isSuccess(), actualResponse.isSuccess());
        assertEquals(postResponse.getStatusCode(), actualResponse.getStatusCode());
    }

    @Test
    public void putDocumentResponseTest() {
        Response<String> putResponse = ResponseImpl.success(200);
        when(elasticResource.putDocument("index", "type", "id", "{json:\"update!\"}")).thenReturn(putResponse);

        Response<String> actualResponse = elasticResource.putDocument("index", "type", "id", "{json:\"update!\"}");
        assertEquals(putResponse.isSuccess(), actualResponse.isSuccess());
        assertEquals(putResponse.getStatusCode(), actualResponse.getStatusCode());
    }

    @Test
    public void deleteDocumentResponseTest() {
        Response<String> deleteResponse = ResponseImpl.success(200);
        when(elasticResource.deleteDocument("index", "type", "id")).thenReturn(deleteResponse);

        Response<String> actualResponse = elasticResource.deleteDocument("index", "type", "id");
        assertEquals(deleteResponse.isSuccess(), actualResponse.isSuccess());
        assertEquals(deleteResponse.getStatusCode(), actualResponse.getStatusCode());
    }
}
