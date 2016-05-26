package com.doerksen.elasticsearch_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ElasticSearchServiceConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("dataLocation")
    private String dataLocation;

    public String getDataLocation() {
        return dataLocation;
    }

    @Valid
    @NotNull
    @JsonProperty("host")
    private String host;

    @Valid
    @NotNull
    @JsonProperty("port")
    private int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
