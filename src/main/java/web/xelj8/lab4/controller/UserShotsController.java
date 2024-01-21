package web.xelj8.lab4.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import web.xelj8.lab4.http.requests.ShotCreateRequest;
import web.xelj8.lab4.http.requests.ShotsGetRequest;
import web.xelj8.lab4.http.resources.ShotResource;
import web.xelj8.lab4.model.Shot;
import web.xelj8.lab4.model.User;
import web.xelj8.lab4.repository.ShotRepository;
import web.xelj8.lab4.repository.UserRepository;

import java.util.List;

@Path("/my-shots")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserShotsController {
    @Inject
    private ShotRepository shotRepository;
    @Inject
    private UserRepository userRepository;

    @POST
    public List<ShotResource> retrieveAll(@Valid ShotsGetRequest requestData) {
        User user = userRepository.findByUsername(requestData.getUsername());
        if (user == null || !user.checkPassword(requestData.getPassword()))
            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);

        List<Shot> shots = shotRepository.findByUser(user);
        return ShotResource.list(shots);
    }

    @Path("/create")
    @POST
    public ShotResource create(@Valid ShotCreateRequest requestData) {
        User user = userRepository.findByUsername(requestData.getUsername());
        if (user == null || !user.checkPassword(requestData.getPassword()))
            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);

        Shot shot = Shot.builder()
                .x(requestData.getX())
                .y(requestData.getY())
                .r(requestData.getR())
                .user(user).build();
        shot = shotRepository.save(shot);

        return new ShotResource(shot);
    }
}
