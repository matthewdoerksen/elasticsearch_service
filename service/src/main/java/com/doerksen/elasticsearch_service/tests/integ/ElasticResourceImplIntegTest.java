package com.doerksen.elasticsearch_service.tests.integ;

import com.doerksen.elasticsearch_service.ElasticSearchService;
import com.doerksen.elasticsearch_service.ElasticSearchServiceConfiguration;
import com.doerksen.elasticsearch_service.resources.ElasticResource;
import com.doerksen.elasticsearch_service.resources.impl.ElasticResourceImpl;
import com.doerksen.utilities.MessageFormatter;
import com.doerksen.utilities.Response;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Environment;
import org.apache.http.HttpStatus;
import org.codehaus.plexus.util.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.Validation;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ElasticResourceImplIntegTest {

    private static ElasticResource elasticResource;

    @BeforeClass
    public static void before() throws Exception {
        ConfigurationFactory<ElasticSearchServiceConfiguration> configurationFactory = new ConfigurationFactory<>(
                ElasticSearchServiceConfiguration.class,
                Validation.buildDefaultValidatorFactory().getValidator(),
                Jackson.newObjectMapper(),
                "");

        ElasticSearchServiceConfiguration configuration = configurationFactory.build(Paths.get("/Users/mdoerksen/git_projects/elasticsearch_service/client/src/main/resources/config/modules/elasticsearch_service/integ_config.yaml").toFile());
        Environment environment = new Environment("integration_test", null, null, null, null);

        /*
            Clean up before hand to make sure that no data is in the index
            Safety guard over top to make sure that we only delete if we're running against integration
         */
        if (configuration.getClusterName().equals("integration_testing_not_for_general_use")) {
            FileUtils.deleteDirectory(configuration.getDataLocation());
        } else {
            throw new IllegalStateException("You are not allowed to wide all data from the Elastic Cluster unless you are running as an integration test");
        }

        ElasticSearchService searchService = new ElasticSearchService();
        searchService.run(configuration, environment);

        elasticResource = new ElasticResourceImpl(searchService.getClient());
    }

    @Test
    public void getDocumentExceptionResponseTest() {
        Response<String> getResponse = elasticResource.getDocument("index", "type", "1");
        assertTrue(getResponse.isError());
        assertEquals(HttpStatus.SC_NOT_FOUND, getResponse.getStatusCode());
        assertEquals("No document found for index index, type type, and id 1.", getResponse.getErrorMsg());
    }

    @Test
    public void postThenGetDocumentResponseTest() {
        Response<String> postResponse = elasticResource.postDocument("index", "type", "3", "{json:\"what a document 3!\"}");
        assertTrue(postResponse.isSuccess());
        assertEquals(HttpStatus.SC_CREATED, postResponse.getStatusCode());
        assertEquals("success", postResponse.getObject());

        Response<String> getResponse = elasticResource.getDocument("index", "type", "3");
        assertTrue(getResponse.isSuccess());
        assertEquals(HttpStatus.SC_OK, getResponse.getStatusCode());
        assertEquals("{json:\"what a document 3!\"}", getResponse.getObject());
    }

    @Test
    public void postDocumentResponseTest() {
        Response<String> postResponse = elasticResource.postDocument("index", "type", "2", "{json:\"what a document!\"}");
        assertTrue(postResponse.isSuccess());
        assertEquals(HttpStatus.SC_CREATED, postResponse.getStatusCode());
        assertEquals("success", postResponse.getObject());
    }

    @Test
    public void postDocumentAlreadyExistsExceptionResponseTest() {
        Response<String> postResponse = elasticResource.postDocument("index", "type", "4", "{json:\"what a document 4!\"}");
        assertTrue(postResponse.isSuccess());
        assertEquals(HttpStatus.SC_CREATED, postResponse.getStatusCode());
        assertEquals("success", postResponse.getObject());

        Response<String> secondPost = elasticResource.postDocument("index", "type", "4", "{json:\"what a document!\"}");
        assertTrue(secondPost.isError());
        assertEquals(HttpStatus.SC_CONFLICT, secondPost.getStatusCode());
        String errorMsg = MessageFormatter.format("Unable to create document {} for index {}, type {} and id {} because the document already exists. Use put to update the document.", "{json:\"what a document!\"}", "index", "type", "4");
        assertEquals(errorMsg, secondPost.getObject());
    }

    @Test
    public void postBadJsonDocumentExceptionResponseTest() {
        Response<String> postResponse = elasticResource.postDocument("index", "type", "5", "document");
        assertTrue(postResponse.isError());
        assertEquals(HttpStatus.SC_BAD_REQUEST, postResponse.getStatusCode());
        String errorMsg = MessageFormatter.format("Error creating document document for index {}, type {} and id {} because the JSON is malformed.", "index", "type", "5");
        assertEquals(errorMsg, postResponse.getErrorMsg());
    }

    @Test
    public void putDocumentDoesNotExistExceptionResponseTest() {
        Response<String> putResponse = elasticResource.putDocument("index", "type", "6", "document");
        assertTrue(putResponse.isError());
        assertEquals(HttpStatus.SC_NOT_FOUND, putResponse.getStatusCode());
        String errorMsg = MessageFormatter.format("Unable to put document {} for index {}, type {} and id {} because the document does not exist. Post the document before attempting to update it.", "document", "index", "type", "6");
        assertEquals(errorMsg, putResponse.getObject());
    }

    @Test
    public void postThenPutDocumentResponseTest() throws InterruptedException {
        Response<String> postResponse = elasticResource.postDocument("index", "type", "8", "{\"key\":\"what a document!\"}");
        assertTrue(postResponse.isSuccess());
        assertEquals(HttpStatus.SC_CREATED, postResponse.getStatusCode());

        Response<String> putResponse = elasticResource.putDocument("index", "type", "8", "{\"key\":\"document_update\"}");
        assertTrue(putResponse.isSuccess());
        assertEquals(HttpStatus.SC_OK, putResponse.getStatusCode());

        Response<String> getResponse = elasticResource.getDocument("index", "type", "8");
        assertTrue(getResponse.isSuccess());
        assertEquals(HttpStatus.SC_OK, getResponse.getStatusCode());
        assertEquals("{\"key\":\"document_update\"}", getResponse.getObject());
    }

    @Test
    public void deleteNonExistentDocumentExceptionResponseTest() {
        Response<String> deleteResponse = elasticResource.deleteDocument("index", "type", "9");
        assertTrue(deleteResponse.isError());
        assertEquals(HttpStatus.SC_NOT_FOUND, deleteResponse.getStatusCode());
        String errorMsg = MessageFormatter.format("Unable to delete document for index {}, type {} and id {} because it does not exist.", "index", "type", "9");
        assertEquals(errorMsg, deleteResponse.getObject());
    }

    @Test
    public void deleteDocumentResponseTest() {
        Response<String> postResponse = elasticResource.postDocument("index", "type", "10", "{json:\"what a document 4!\"}");
        assertTrue(postResponse.isSuccess());
        assertEquals(HttpStatus.SC_CREATED, postResponse.getStatusCode());
        assertEquals("success", postResponse.getObject());

        Response<String> deleteResponse = elasticResource.deleteDocument("index", "type", "10");
        assertTrue(deleteResponse.isSuccess());
        assertEquals(HttpStatus.SC_OK, deleteResponse.getStatusCode());
    }
}
