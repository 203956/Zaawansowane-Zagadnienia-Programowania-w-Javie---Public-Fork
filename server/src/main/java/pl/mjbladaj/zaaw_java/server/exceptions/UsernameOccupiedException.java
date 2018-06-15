package pl.mjbladaj.zaaw_java.server.exceptions;

public class UsernameOccupiedException extends Exception {
    public UsernameOccupiedException() {
    }

    public UsernameOccupiedException(String message) {
        super(message);
    }

    public UsernameOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameOccupiedException(Throwable cause) {
        super(cause);
    }

    public UsernameOccupiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
