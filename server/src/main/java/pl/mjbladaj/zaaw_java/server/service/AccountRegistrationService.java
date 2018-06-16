package pl.mjbladaj.zaaw_java.server.service;

import pl.mjbladaj.zaaw_java.server.dto.UserRegistrationData;
import pl.mjbladaj.zaaw_java.server.exceptions.InvalidCredentialsException;
import pl.mjbladaj.zaaw_java.server.exceptions.UsernameOccupiedException;

public interface AccountRegistrationService {
    UserRegistrationData register(UserRegistrationData userRegistrationData)
            throws UsernameOccupiedException, InvalidCredentialsException;
}
