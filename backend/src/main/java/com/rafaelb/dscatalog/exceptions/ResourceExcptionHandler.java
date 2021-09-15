package com.rafaelb.dscatalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExcptionHandler {

    @ExceptionHandler(EntityNotFoundExcptions.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundExcptions e, HttpServletRequest request){
            StandardError err = new StandardError();
            err.setTimestamp(Instant.now());
            err.setStatus(HttpStatus.NOT_FOUND.value());
            err.setError("Resource Not Found");
            err.setMessage(e.getMessage());
            err.setPath(request.getRequestURI());
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(err);
    }
}