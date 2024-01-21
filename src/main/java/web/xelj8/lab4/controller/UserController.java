package web.xelj8.lab4.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import web.xelj8.lab4.http.requests.UserAuthRequest;
import web.xelj8.lab4.http.resources.UserResource;
import web.xelj8.lab4.model.User;
import web.xelj8.lab4.repository.UserRepository;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    private UserRepository repository;

    @POST @Path("/register")
    public UserResource register(@Valid UserAuthRequest requestData) {
        if (repository.findByUsername(requestData.getUsername()) != null)
            throw new WebApplicationException("User with this username already exists", Response.Status.CONFLICT);

        User user = User.builder()
                .username(requestData.getUsername())
                .password(requestData.getPassword()).build();
        repository.save(user);

        return new UserResource(user);
    }

    @POST @Path("/login")
    public UserResource login(@Valid UserAuthRequest requestData) {
        User user = repository.findByUsername(requestData.getUsername());
        if (user == null || !user.checkPassword(requestData.getPassword()))
            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);

        return new UserResource(user);
    }
}
