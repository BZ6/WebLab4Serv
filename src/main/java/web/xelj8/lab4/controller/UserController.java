package web.xelj8.lab4.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import web.xelj8.lab4.http.requests.UserAuthRequest;
import web.xelj8.lab4.model.User;
import web.xelj8.lab4.repository.UserRepository;
import web.xelj8.lab4.security.JwtProvider;
import web.xelj8.lab4.exceptions.ServerException;

@Path("/auth")
@Slf4j
public class UserController {
    @Inject
    private UserRepository repository;

    @Inject
    private JwtProvider jwtProvider;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Valid UserAuthRequest requestData) {
        if (repository.findByUsername(requestData.getUsername()) != null) {
            throw new WebApplicationException("User with this username already exists", Response.Status.CONFLICT);
        }

        User user = User.builder()
                .username(requestData.getUsername())
                .password(requestData.getPassword()).build();
        repository.save(user);
        try {
            return Response.ok(jwtProvider.generateToken(user.getUsername())).build();
        } catch (ServerException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid UserAuthRequest requestData) {
        User user = repository.findByUsername(requestData.getUsername());
        if (user == null || !user.checkPassword(requestData.getPassword())) {
            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);
        }

        try {
            return Response.ok(jwtProvider.generateToken(user.getUsername())).build();
        } catch (ServerException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
