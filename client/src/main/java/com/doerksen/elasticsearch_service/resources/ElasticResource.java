package com.doerksen.elasticsearch_service.resources;

import com.doerksen.utilities.Response;
import com.newrelic.api.agent.Trace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/elastic")
public interface ElasticResource {

    @Trace
    @GET
    @Path("/{index}/{type}/{id}")
    Response<String> getDocument(@PathParam("index") final String index,
                                 @PathParam("type") final String type,
                                 @PathParam("id") final String id);

    @Trace
    @POST
    @Path("/{index}/{type}/{id}")
    Response<String> postDocument(@PathParam("index") final String index,
                                  @PathParam("type") final String type,
                                  @PathParam("id") final String id,
                                  String documentJSON);

    @Trace
    @PUT
    @Path("/{index}/{type}/{id}")
    Response<String> putDocument(@PathParam("index") final String index,
                                 @PathParam("type") final String type,
                                 @PathParam("id") final String id,
                                 String documentJSON);

    @Trace
    @DELETE
    @Path("/{index}/{type}/{id}")
    Response<String> deleteDocument(@PathParam("index") final String index,
                                    @PathParam("type") final String type,
                                    @PathParam("id") final String id);
}
