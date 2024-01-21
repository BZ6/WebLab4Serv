package web.xelj8.lab4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import web.xelj8.lab4.security.PasswordEncrypter;

@Entity @Table(name = "User_")
@Getter @Setter(value = AccessLevel.PACKAGE)
@Builder @NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PACKAGE)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotNull @Pattern(regexp = "[a-zA-Z0-9_]+")
    private String username;
    @Column(nullable = false)
    @NotNull
    private String password;

    public static class UserBuilder {
        public UserBuilder password(String password) {
            this.password = _encodePassword(password);
            return this;
        }
    }

    void setPassword(String password) {
        this.password = _encodePassword(password);
    }

    private static String _encodePassword(String password) {
        return PasswordEncrypter.encrypt(password);
    }

    public boolean checkPassword(String password) {
        return PasswordEncrypter.checkPassword(password, this.password);
    }
}
