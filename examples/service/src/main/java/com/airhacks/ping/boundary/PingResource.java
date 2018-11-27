package com.airhacks.ping.boundary;

import co.pablob.security.commons.entity.JwtPrincipal;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * @author airhacks.com
 */
@Path("ping")
public class PingResource {

    @Inject
    private JwtPrincipal principal;

    @GET
    public Response ping() {

        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("name", ofNullable(principal.getName()).orElse(""))
                .add("userName", ofNullable(principal.getUserName()).orElse(""))
                .add("fullName", ofNullable(principal.getFullName()).orElse(""))
                .add("givenName", ofNullable(principal.getGivenName()).orElse(""))
                .add("familyName", ofNullable(principal.getFamilyName()).orElse(""))
                .add("email", ofNullable(principal.getEmail()).orElse(""))
                .add("picture", ofNullable(principal.getPicture()).orElse(""))
                .add("token", ofNullable(principal.getToken()).orElse(""))
                .build();

        return Response.ok(jsonResponse).build();
    }

}
