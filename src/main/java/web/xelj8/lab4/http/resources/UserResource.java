package web.xelj8.lab4.http.resources;

import lombok.Getter;
import web.xelj8.lab4.model.User;

@Getter
public class UserResource {
    Long id;
    String username;

    public UserResource(User user) {
        id = user.getId();
        username = user.getUsername();
    }
}
