package com.game.transformer.exception.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.transformer.exception.TransformerException;
import com.game.transformer.exception.TransformerNotFoundException;
import com.game.transformer.model.ServiceError;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * This class provides the centralized exception handling across Transformer Service
 * <p/>
 * This class provides an {@code @ExceptionHandler} method for handling different exceptions. Exception Handling methods
 * will returns a {@code ResponseEntity} with appropriate status code and error messages
 *
 * @author Javalingappa
 */

@Log
@ControllerAdvice
@RestController
public class TransformerGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    ObjectMapper objectMapper;


    /**
     * Handler to wrap {@link TransformerNotFoundException} and enriches them with a custom error code and response body.
     *
     * @param exception instance of exception to be handled
     * @return the {@ResponseEntity} containing custom error codes and messages
     */
    @ExceptionHandler(TransformerNotFoundException.class)
    public final ResponseEntity<ServiceError> handleProductResultNotFoundException(
            TransformerNotFoundException exception) throws IOException {
        ServiceError serviceError = new ServiceError();
        OffsetDateTime ts = OffsetDateTime.now();
        serviceError.setTimestamp(ts);
        Map<String, Object> exceptionMessages = objectMapper.readValue(exception.getMessage(),
                Map.class);
        serviceError.setMessage(String.valueOf(exceptionMessages.get("message")));
        serviceError.setDetails(String.valueOf(exceptionMessages.get("details")));

        return new ResponseEntity<>(serviceError, HttpStatus.NOT_FOUND);
    }


    /**
     * Handler to wrap {@link TransformerException} and enriches them with a custom error code and response body.
     *
     * @param exception instance of exception to be handled
     * @return the {@ResponseEntity} containing custom error codes and messages
     */
    @ExceptionHandler(TransformerException.class)
    public final ResponseEntity<ServiceError> handleProductException(TransformerException exception) throws IOException {
        ServiceError serviceError = new ServiceError();
        OffsetDateTime ts = OffsetDateTime.now();
        serviceError.setTimestamp(ts);
        Map<String, Object> exceptionMessages = objectMapper.readValue(exception.getMessage(), Map.class);
        serviceError.setMessage(String.valueOf(exceptionMessages.get("message")));
        serviceError.setDetails(String.valueOf(exceptionMessages.get("details")));

        return new ResponseEntity<>(serviceError, HttpStatus.BAD_REQUEST);
    }

}