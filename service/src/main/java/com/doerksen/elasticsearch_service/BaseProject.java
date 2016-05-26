package com.doerksen.elasticsearch_service;

import com.doerksen.elasticsearch_service.resources.impl.ElasticResourceImpl;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import java.net.InetAddress;

public class BaseProject extends Application<BaseProjectConfiguration> {

    private static Client elasticSearchCluster;

    public static void main(String[] args) throws Exception {
        new BaseProject().run(args);
    }

    @Override
    public void run(BaseProjectConfiguration configuration, Environment environment) throws Exception {

        // TODO - work on getting a cluster up and running and verifying if
        //        these settings make sense for a clu

        Node elasticNode = NodeBuilder.nodeBuilder()
                .data(true)
                .settings(Settings.builder()
                        .put("path.home", configuration.getElasticDataLocation()))
                .build();

        elasticNode.start();
        Settings settings = Settings.settingsBuilder()
                .put("client.transport.sniff", true)
                .build();

        elasticSearchCluster = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(configuration.getElasticHost()), configuration.getElasticPort()));

        environment.jersey().register(new ElasticResourceImpl());
    }

    public static Client elasticSearchCluster() {
        return elasticSearchCluster;
    }
}
