package io.descoped.container.support;

/**
 * Created by oranheim on 13/12/2016.
 */
public class DescopedServerException extends RuntimeException {
    public DescopedServerException() {
    }

    public DescopedServerException(String message) {
        super(message);
    }

    public DescopedServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DescopedServerException(Throwable cause) {
        super(cause);
    }

    public DescopedServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
