package pl.mjbladaj.zaaw_java.server.exceptions;

public class AccountStateException extends Exception{
    public AccountStateException() {
    }

    public AccountStateException(String message) {
        super(message);
    }

    public AccountStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountStateException(Throwable cause) {
        super(cause);
    }

    public AccountStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
