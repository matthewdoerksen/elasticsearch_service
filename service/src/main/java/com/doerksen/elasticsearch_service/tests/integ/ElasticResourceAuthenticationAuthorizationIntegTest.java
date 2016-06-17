package com.doerksen.elasticsearch_service.tests.integ;

import com.doerksen.elasticsearch_service.ElasticSearchService;
import com.doerksen.elasticsearch_service.ElasticSearchServiceConfiguration;
import com.doerksen.elasticsearch_service.resources.ElasticResource;
import com.doerksen.utilities.ResourceWebTargetBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;

/**
 * NOTE: cannot be run simultaneously with ElasticResourceImplIntegTest because it also spins up the ES cluster.
 */
public class ElasticResourceAuthenticationAuthorizationIntegTest {

    @ClassRule
    public static final DropwizardAppRule<ElasticSearchServiceConfiguration> RULE = new DropwizardAppRule<>(ElasticSearchService.class, "/Users/mdoerksen/git_projects/elasticsearch_service/client/src/main/resources/config/modules/elasticsearch_service/integ_config.yaml");

    @Test(expected = NotAuthorizedException.class)
    public void getDocumentMissingAuthenticationCredentialsTest() {
        ElasticResource externalClient = new ResourceWebTargetBuilder<>(ElasticResource.class,
                "localhost",
                "10000")
        .build();

        externalClient.getDocument("index", "type", "1");
    }

    @Test(expected = ForbiddenException.class)
    public void getDocumentNotAuthorizedTest() {
        ElasticResource externalClient = new ResourceWebTargetBuilder<>(ElasticResource.class,
                "localhost",
                "10000")
                .withCredentials("integ", "12345")
                .build();

        externalClient.getDocument("users", "user", "1");
    }

    @Test
    public void getDocumentValidAuthenticationAndAuthorizationTest() {
        ElasticResource externalClient = new ResourceWebTargetBuilder<>(ElasticResource.class,
                "localhost",
                "10000")
                .withCredentials("integ", "123")
                .build();

        externalClient.getDocument("users", "user", "1");
    }
}
