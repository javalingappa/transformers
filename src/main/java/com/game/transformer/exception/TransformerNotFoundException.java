package com.game.transformer.exception;
/**
 * @author Javalingappa
 */
public class TransformerNotFoundException extends RuntimeException {

    public TransformerNotFoundException() {
        super();
    }

    public TransformerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TransformerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformerNotFoundException(String message) {
        super(message);
    }

    public TransformerNotFoundException(Throwable cause) {
        super(cause);
    }
}
