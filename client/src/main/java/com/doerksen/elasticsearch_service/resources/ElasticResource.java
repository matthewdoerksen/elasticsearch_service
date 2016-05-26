package com.doerksen.elasticsearch_service.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/elastic")
public interface ElasticResource {

    // TODO - replace me with a Response wrapper so that we can return the
    //        GetResponse from elastic (which we can't directly here because
    //        a BadRequestException gets thrown).

    @GET
    @Path("/{index}/{type}/{id}")
    Map<String, Object> getDocument(@PathParam("index") String index,
                                    @PathParam("type") String type,
                                    @PathParam("id") String id);
}
