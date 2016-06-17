package com.doerksen.elasticsearch_service;

import com.doerksen.elasticsearch_service.resources.impl.AuthorizationValidator;
import com.doerksen.elasticsearch_service.resources.impl.AuthorizationValidator.ACCESS_TYPE;
import com.doerksen.utilities.MessageFormatter;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Tuple;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.util.Optional;

public class AuthenticationFilter implements ContainerRequestFilter {

    private static ACCESS_TYPE getAccessTypeForMethod(final String method) {
        Preconditions.checkArgument(StringUtils.isNotBlank(method));
        switch (method) {
            case "GET":
                return ACCESS_TYPE.READ;
            case "DELETE":
                return ACCESS_TYPE.DELETE;
            case "POST":
                return ACCESS_TYPE.CREATE;
            case "PUT":
                return ACCESS_TYPE.UPDATE;
            default:
                throw new IllegalStateException(MessageFormatter.format("Unable to map request type {} to one of CREATE, READ, UPDATE or DELETE.", method));
        }
    }

    /**
     * Pull out the application name and token passed in the header of every request and make sure
     * that the name + token combo exists and that it has the correct permissions to the requested resource.
     * @param requestContext
     * @throws IOException
     */
    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        Optional<String> clientName = Optional.ofNullable(requestContext.getHeaders().getFirst("app_name"));
        Optional<String> accessToken = Optional.ofNullable(requestContext.getHeaders().getFirst("access_token"));

        if (!clientName.isPresent() || !accessToken.isPresent()) {
            throw new AuthenticationException("Client name and/or API key was invalid. Remember to set them in the request header.");
        }

        // pull out the elasticsearch index and type from the URI so we can check that we have the correct permissions
        // useable index and type (but may not actually exist) if we get past this
        Tuple<String, String> indexAndType = parseUri(requestContext.getUriInfo().getPath());

        // what kind of operation are we trying to do?
        ACCESS_TYPE accessType = getAccessTypeForMethod(requestContext.getMethod());

        // check to see that the application name and access token have the correct
        // permissions (create, read, update, delete) for the resource we want to access
        AuthorizationValidator.isAuthorized(clientName.get(), accessToken.get(), accessType, indexAndType);
    }

    /**
     * Pull out the index and type that we're trying to access within elastic
     * @param pathRequested - full path, e.g. elastic/users(index)/user(type)/1
     * @return
     * @throws NotAuthorizedException
     */
    private Tuple<String, String> parseUri(final String pathRequested) throws NotAuthorizedException {
        String requestedResource = pathRequested;

        final String baseResourceName = requestedResource.substring(0, requestedResource.indexOf("/"));
        requestedResource = requestedResource.substring(baseResourceName.length() + 1);

        final String indexName = requestedResource.substring(0, requestedResource.indexOf("/"));
        requestedResource = requestedResource.substring(indexName.length() + 1);

        final String typeName = requestedResource.substring(0, requestedResource.indexOf("/"));
        requestedResource = requestedResource.substring(typeName.length() + 1);

        // make sure that the path matches up with the original and that nothing has been modified
        final String fullPathRebuilt = Joiner.on("/").join(baseResourceName, indexName, typeName, requestedResource);
        if (pathRequested.compareTo(fullPathRebuilt) != 0) {
            throw new NotAuthorizedException("URL requested has been modified and does not match the original.");
        }

        return new Tuple<>(indexName, typeName);
    }
}