package pl.mjbladaj.zaaw_java.server.exceptions;

public class InvalidCredentialsExeption extends Exception {
    public InvalidCredentialsExeption() {
    }

    public InvalidCredentialsExeption(String message) {
        super(message);
    }

    public InvalidCredentialsExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsExeption(Throwable cause) {
        super(cause);
    }

    public InvalidCredentialsExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
