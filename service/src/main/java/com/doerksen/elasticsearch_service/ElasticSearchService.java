package com.doerksen.elasticsearch_service;

import com.doerksen.elasticsearch_service.auth.AuthenticationFilter;
import com.doerksen.elasticsearch_service.resources.ElasticResource;
import com.doerksen.elasticsearch_service.resources.impl.ElasticResourceImpl;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.NodeBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticSearchService extends Application<ElasticSearchServiceConfiguration> {

    private Client esCluster;

    public static void main(String[] args) throws Exception {
        new ElasticSearchService().run(args);
    }

    @Override
    public void run(ElasticSearchServiceConfiguration configuration, Environment environment) throws Exception {

        // TODO - work on getting a cluster up and running and verifying if these settings make sense for a cluster

        configureAndStartNode(configuration);

        esCluster = configureClusterAndGetClient(configuration);

        // wire up the resource so that NewRelic can send stats back for methods annotated with @Trace
        ElasticResource elasticResource = new ElasticResourceImpl(esCluster);

        environment.jersey().register(elasticResource);

        // register our authentication filter so that we verify the app + token and permissions
        // before we pass the request on to the actual endpoint
        environment.jersey().register(new AuthenticationFilter());
    }

    private void configureAndStartNode(ElasticSearchServiceConfiguration configuration) {
        NodeBuilder.nodeBuilder()
                .data(true)
                .settings(Settings.settingsBuilder()
                        .put("path.home", configuration.getDataLocation())
                        .put("cluster.name", configuration.getClusterName()))
                .build()
                .start();
    }

    private Client configureClusterAndGetClient(ElasticSearchServiceConfiguration configuration) throws UnknownHostException {
        return TransportClient.builder()
                .settings(Settings.settingsBuilder()
                        .put("client.transport.sniff", true)
                        .put("cluster.name", configuration.getClusterName())
                        .build())
                .build()
                .addTransportAddress(new InetSocketTransportAddress(
                        InetAddress.getByName(configuration.getHost()),
                        configuration.getPort()));
    }

    public Client getClient() {
        return esCluster;
    }
}
