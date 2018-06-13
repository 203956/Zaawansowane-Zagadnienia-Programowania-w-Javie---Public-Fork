package pl.mjbladaj.zaaw_java.server.exceptions;

public class TimePeriodNotAvailableException extends Exception {
    public TimePeriodNotAvailableException(String message) {
        super(message);
    }

    public TimePeriodNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimePeriodNotAvailableException(Throwable cause) {
        super(cause);
    }

    public TimePeriodNotAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TimePeriodNotAvailableException() { }
}
