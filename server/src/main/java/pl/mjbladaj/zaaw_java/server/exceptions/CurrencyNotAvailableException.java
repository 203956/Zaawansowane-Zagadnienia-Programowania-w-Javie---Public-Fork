package pl.mjbladaj.zaaw_java.server.exceptions;

public class CurrencyNotAvailableException extends Exception {
    public CurrencyNotAvailableException() {
    }

    public CurrencyNotAvailableException(String message) {
        super(message);
    }

    public CurrencyNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyNotAvailableException(Throwable cause) {
        super(cause);
    }

    public CurrencyNotAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
