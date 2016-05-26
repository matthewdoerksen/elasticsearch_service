package com.doerksen.elasticsearch_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BaseProjectConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("dataPath")
    private String elasticDataLocation;

    public String getElasticDataLocation() {
        return elasticDataLocation;
    }

    @Valid
    @NotNull
    @JsonProperty("elasticHost")
    private String elasticHost;

    @Valid
    @NotNull
    @JsonProperty("elasticPort")
    private int elasticPort;

    public String getElasticHost() {
        return elasticHost;
    }

    public int getElasticPort() {
        return elasticPort;
    }

}
