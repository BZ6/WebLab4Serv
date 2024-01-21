package web.xelj8.lab4.filter;

import web.xelj8.lab4.security.JwtProvider;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import web.xelj8.lab4.security.UserPrincipal;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Provider
@Slf4j
@Priority(Priorities.AUTHORIZATION)
public class JwtAuthFilter implements ContainerRequestFilter {
    @Inject
    private JwtProvider jwtProvider;

    private static final Set<String> SKIP_PATHS = new HashSet<>(Arrays.asList(
            // TODO: Скипнуть авторизацию
            "/auth/register",
            "/auth/login"));

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        if (SKIP_PATHS.contains(path)) {
            return; // Skip JWT check for specified paths
        }
        log.info(path);

        String authorizationHeader = requestContext.getHeaderString("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization token is required")
                    .build());
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length());

        if (jwtProvider.isTokenExpired(token)) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Token expired")
                    .build());
            return;
        }

        String username = jwtProvider.getUsernameFromToken(token);

        if (username == null) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid token")
                    .build());
            return;
        }

        SecurityContext originalContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new UserPrincipal(username);
            }

            @Override
            public boolean isSecure() {
                return originalContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }

            @Override
            public boolean isUserInRole(String role) {
                log.warn("isUserInRole called, but no roles are used in this application.");
                return false;
            }
        });
    }
}
