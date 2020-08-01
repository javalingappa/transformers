package com.game.transformer.exception;
/**
 * @author Javalingappa
 */
public class TransformerException extends RuntimeException {

    public TransformerException() {
        super();
    }

    public TransformerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TransformerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformerException(String message) {
        super(message);
    }

    public TransformerException(Throwable cause) {
        super(cause);
    }
}
