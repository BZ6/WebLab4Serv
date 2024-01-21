package web.xelj8.lab4.http.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShotsGetRequest {
    @NotNull(message = "Username must not be null")
    String username;
    @NotNull(message = "Password must not be null")
    String password;
}
