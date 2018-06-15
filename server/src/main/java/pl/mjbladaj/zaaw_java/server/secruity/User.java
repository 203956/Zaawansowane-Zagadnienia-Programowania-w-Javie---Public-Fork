package pl.mjbladaj.zaaw_java.server.secruity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    private String username;
    private String password;
}