package web.xelj8.lab4.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import web.xelj8.lab4.http.requests.ShotDTO;
import web.xelj8.lab4.http.resources.ShotResource;
import web.xelj8.lab4.model.Shot;
import web.xelj8.lab4.model.User;
import web.xelj8.lab4.repository.ShotRepository;
import web.xelj8.lab4.repository.UserRepository;
import web.xelj8.lab4.security.UserPrincipal;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Context;

import java.util.List;

@Path("/my-shots")
public class UserShotsController {
    @Inject
    private ShotRepository shotRepository;

    @Inject
    private UserRepository userRepository;

    @Context
    private SecurityContext securityContext;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShotResource> retrieveAll() {
        // get username and password from {jwt & db}
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        List<Shot> shots = shotRepository.findByUser(userPrincipal.getName());
        return ShotResource.list(shots);
    }

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ShotResource create(@Valid ShotDTO requestData) {
        // get username and password from {jwt & db}
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        User user = userRepository.findByUsername(userPrincipal.getName());
        Shot shot = Shot.builder()
            .x(requestData.getX())
            .y(requestData.getY())
            .r(requestData.getR())
            .user(user).build();
        return new ShotResource(shotRepository.save(shot));
    }
}
