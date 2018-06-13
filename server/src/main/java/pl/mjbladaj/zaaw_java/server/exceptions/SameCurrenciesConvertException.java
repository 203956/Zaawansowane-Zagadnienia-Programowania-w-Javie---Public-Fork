package pl.mjbladaj.zaaw_java.server.exceptions;

public class SameCurrenciesConvertException extends Exception {

    public SameCurrenciesConvertException(String message) {
        super(message);
    }

    public SameCurrenciesConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public SameCurrenciesConvertException(Throwable cause) {
        super(cause);
    }

    public SameCurrenciesConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SameCurrenciesConvertException() { }
}
