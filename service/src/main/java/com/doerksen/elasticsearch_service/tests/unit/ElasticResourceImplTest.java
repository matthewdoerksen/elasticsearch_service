package com.doerksen.elasticsearch_service.tests.unit;

import com.doerksen.elasticsearch_service.resources.ElasticResource;
import com.doerksen.utilities.MessageFormatter;
import com.doerksen.utilities.Response;
import com.doerksen.utilities.ResponseImpl;
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
     * the ElasticResourceImplIntegTest class.2
     */
    @Mock
    private ElasticResource elasticResource;

    @Before
    public void before() {
        elasticResource = mock(ElasticResource.class);
    }

    @Test()
    public void getDocumentExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Unable to get document for index {}, type {} and id {}.", "index", "type", "id");
        Response<String> getResponseWithErrorMsg = new ResponseImpl<>(errorMsg);
        when(elasticResource.getDocument("index", "type", "id")).thenReturn(getResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.getDocument("index", "type", "id");
        assertEquals(getResponseWithErrorMsg.isError(), actualResponse.isError());
        assertTrue(getResponseWithErrorMsg.getObject().equals(actualResponse.getObject()));
    }

    @Test
    public void postDocumentExceptionResponseTest() {
        String errorMsg = MessageFormatter.format("Unable to create document for index {}, type {} and id {}.", "index", "type", "id");
        Response<String> postResponseWithErrorMsg = new ResponseImpl<>(errorMsg);
        when(elasticResource.postDocument("index", "type", "id", "document")).thenReturn(postResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.postDocument("index", "type", "id", "document");
        assertEquals(postResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(postResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
    }

    @Test
    public void putDocumentExceptionResponseTest() {
        Response<String> putResponseWithErrorMsg = ResponseImpl.error(404);
        when(elasticResource.putDocument("index", "type", "id", "document")).thenReturn(putResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.putDocument("index", "type", "id", "document");
        assertEquals(putResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(putResponseWithErrorMsg.getStatusCode(), actualResponse.getStatusCode());
    }

    @Test
    public void deleteDocumentExceptionResponseTest() {
        Response<String> deleteResponseWithErrorMsg = ResponseImpl.error(404);
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
