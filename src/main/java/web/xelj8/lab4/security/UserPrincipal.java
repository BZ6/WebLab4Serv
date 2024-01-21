package web.xelj8.lab4.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.security.Principal;

@Data
@Builder
@AllArgsConstructor
public class UserPrincipal implements Principal {
    private final String name;
}