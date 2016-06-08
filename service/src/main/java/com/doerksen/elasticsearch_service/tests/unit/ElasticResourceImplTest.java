package com.doerksen.elasticsearch_service.tests.unit;

import com.doerksen.elasticsearch_service.resources.ElasticResource;
import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

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

    /*@Test()
    public void getDocumentExceptionResponseTest() {
        String errorMsg = String.format("Unable to get document for index {}, type {} and id {}.", "index", "type", "id");
        Response<String> getResponseWithErrorMsg = new ResponseImpl<>(errorMsg);
        when(elasticResource.getDocument("index", "type", "id")).thenReturn(getResponseWithErrorMsg);

        Response<String> actualResponse = elasticResource.getDocument("index", "type", "id");
        assertEquals(getResponseWithErrorMsg.isError(), actualResponse.isError());
        assertTrue(getResponseWithErrorMsg.getObject().equals(actualResponse.getObject()));
    }*/

    /*@Test
    public void postDocumentExceptionResponseTest() {
        Response<String> postResponseWithErrorMsg = new ResponseImpl<>("Unable to create document for index {}, type {} and id {}.", "index", "type", "id");
        when(elasticResource.postDocument("index", "type", "id", "document")).thenReturn(postResponseWithErrorMsg);

        Response<Boolean> actualResponse = elasticResource.postDocument("index", "type", "id", "document");
        assertEquals(postResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(postResponseWithErrorMsg.statusCode(), actualResponse.statusCode());
    }

    @Test
    public void putDocumentExceptionResponseTest() {
        Response<Boolean> putResponseWithErrorMsg = ResponseImpl.error(404);
        when(elasticResource.putDocument("index", "type", "id", "document")).thenReturn(putResponseWithErrorMsg);

        Response<Boolean> actualResponse = elasticResource.putDocument("index", "type", "id", "document");
        assertEquals(putResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(putResponseWithErrorMsg.statusCode(), actualResponse.statusCode());
    }

    @Test
    public void deleteDocumentExceptionResponseTest() {
        Response<Boolean> deleteResponseWithErrorMsg = ResponseImpl.error(404);
        when(elasticResource.deleteDocument("index", "type", "id")).thenReturn(deleteResponseWithErrorMsg);

        Response<Boolean> actualResponse = elasticResource.deleteDocument("index", "type", "id");
        assertEquals(deleteResponseWithErrorMsg.isError(), actualResponse.isError());
        assertEquals(deleteResponseWithErrorMsg.statusCode(), actualResponse.statusCode());
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
        Response<Boolean> postResponse = ResponseImpl.success();
        when(elasticResource.postDocument("index", "type", "id", "{json:\"what a document!\"}")).thenReturn(postResponse);

        Response<Boolean> actualResponse = elasticResource.postDocument("index", "type", "id", "{json:\"what a document!\"}");
        assertEquals(postResponse.isSuccess(), actualResponse.isSuccess());
        assertEquals(postResponse.statusCode(), actualResponse.statusCode());
    }

    @Test
    public void putDocumentResponseTest() {
        Response<Boolean> putResponse = ResponseImpl.success();
        when(elasticResource.putDocument("index", "type", "id", "{json:\"update!\"}")).thenReturn(putResponse);

        Response<Boolean> actualResponse = elasticResource.putDocument("index", "type", "id", "{json:\"update!\"}");
        assertEquals(putResponse.isSuccess(), actualResponse.isSuccess());
        assertEquals(putResponse.statusCode(), actualResponse.statusCode());
    }

    @Test
    public void deleteDocumentResponseTest() {
        Response<Boolean> deleteResponse = ResponseImpl.success();
        when(elasticResource.deleteDocument("index", "type", "id")).thenReturn(deleteResponse);

        Response<Boolean> actualResponse = elasticResource.deleteDocument("index", "type", "id");
        assertEquals(deleteResponse.isSuccess(), actualResponse.isSuccess());
        assertEquals(deleteResponse.statusCode(), actualResponse.statusCode());
    }*/
}
