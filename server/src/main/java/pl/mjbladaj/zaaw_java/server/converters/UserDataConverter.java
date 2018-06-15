package pl.mjbladaj.zaaw_java.server.converters;

import pl.mjbladaj.zaaw_java.server.dto.UserRegistrationData;
import pl.mjbladaj.zaaw_java.server.entity.Account;

public abstract class UserDataConverter {
    public static Account getAccount(UserRegistrationData userRegistrationData) {
        return Account
                .builder()
                .login(userRegistrationData.getUsername())
                .password(userRegistrationData.getPassword())
                .mail(userRegistrationData.getMail())
                .build();
    }
}
