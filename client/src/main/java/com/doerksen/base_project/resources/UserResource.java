package com.doerksen.base_project.resources;

import com.doerksen.base_project.dto.UserDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/user")
public interface UserResource {
    @GET
    @Path("/{id}")
    UserDto getUser(@PathParam("id") long id);
}
