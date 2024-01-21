package web.xelj8.lab4.exceptions;

import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotSupportedExceptionMapper implements RestExceptionMapper<NotSupportedException> {
    public Response toResponse(NotSupportedException e) {
        return buildResponse("Invalid request data format");
    }
}
