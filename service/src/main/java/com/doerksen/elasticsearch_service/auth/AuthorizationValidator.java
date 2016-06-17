package com.doerksen.elasticsearch_service.auth;

import com.google.common.collect.Sets;
import org.apache.http.HttpStatus;
import org.elasticsearch.common.collect.Tuple;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthorizationValidator {

    public enum ACCESS_TYPE {
        READ,
        CREATE,
        UPDATE,
        DELETE
    }

    // hash code (app name + token) -> map of resource endpoints (e.g. users index and user type in elastic) -> permission types (read, write, delete, update)
    // TODO - make this unmodifiable, permit override in dev (full access)
    // TODO - move this to a database so that we don't have to hard code access
    private static final Map<Integer, Map<Tuple<String, String>, Set<ACCESS_TYPE>>> temp = new HashMap<Integer, Map<Tuple<String, String>, Set<ACCESS_TYPE>>>() {{
        this.put(("Combinatrix" + "1234").hashCode(), new HashMap<Tuple<String, String>, Set<ACCESS_TYPE>>() {{
            this.put(new Tuple<>("users", "user"), Sets.newHashSet(ACCESS_TYPE.CREATE, ACCESS_TYPE.DELETE, ACCESS_TYPE.READ, ACCESS_TYPE.UPDATE));
            // this enables creating a brand new index and/or type
            // you need to register the index and type before you are allowed to access it (including creation)
            this.put(new Tuple<>("temp", "user"), Sets.newHashSet(ACCESS_TYPE.values()));
        }});
        this.put(("Combinatrix" + "123").hashCode(), new HashMap<Tuple<String, String>, Set<ACCESS_TYPE>>() {{
            this.put(new Tuple<>("users", "user"), Sets.newHashSet(ACCESS_TYPE.CREATE, ACCESS_TYPE.DELETE));
        }});
        this.put(("Combinatrix" + "12345").hashCode(), new HashMap<Tuple<String, String>, Set<ACCESS_TYPE>>() {{
            this.put(new Tuple<>("users", "user"), Sets.newHashSet(ACCESS_TYPE.READ));
        }});


        this.put(("integ" + "12345").hashCode(), new HashMap<Tuple<String, String>, Set<ACCESS_TYPE>>() {{
            this.put(new Tuple<>("users", "user"), Sets.newHashSet(ACCESS_TYPE.CREATE));
        }});
        this.put(("integ" + "123").hashCode(), new HashMap<Tuple<String, String>, Set<ACCESS_TYPE>>() {{
            this.put(new Tuple<>("users", "user"), Sets.newHashSet(ACCESS_TYPE.READ));
        }});
    }};

    public static boolean isAuthorized(final String clientName,
                                       final String accessToken,
                                       final ACCESS_TYPE accessType,
                                       final Tuple<String, String> indexAndType) throws NotAuthorizedException {
        Set<ACCESS_TYPE> grantedPermissions = temp.getOrDefault((clientName + accessToken).hashCode(), new HashMap<>()).getOrDefault(indexAndType, new HashSet<>());
        if (!grantedPermissions.contains(accessType)) {
            throw new WebApplicationException("Application/API key is not authorized to perform the request.", HttpStatus.SC_FORBIDDEN);
        }

        return true;
    }
}
