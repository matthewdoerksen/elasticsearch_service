package com.doerksen.elasticsearch_service.resources.impl;

import com.doerksen.elasticsearch_service.BaseProject;
import com.doerksen.elasticsearch_service.resources.ElasticResource;
import org.elasticsearch.action.get.GetResponse;

import java.util.Map;

public class ElasticResourceImpl implements ElasticResource {

    public ElasticResourceImpl() { }

    // TODO - replace me with a Response wrapper so that we can return the
    //        GetResponse from elastic (which we can't directly here because
    //        a BadRequestException gets thrown).

    public Map<String, Object> getDocument(String index,
                                           String type,
                                           String id) {
        GetResponse response = BaseProject.elasticSearchCluster().prepareGet(index, type, id).get();
        return response.getSourceAsMap();
    }
}
