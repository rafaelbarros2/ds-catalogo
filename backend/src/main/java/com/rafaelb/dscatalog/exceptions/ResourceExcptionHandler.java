package com.rafaelb.dscatalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExcptionHandler {

    @ExceptionHandler(ResourceNotFoundExcptions.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundExcptions e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
            StandardError err = new StandardError();
            err.setTimestamp(Instant.now());
            err.setStatus(status.value());
            err.setError("Resource Not Found");
            err.setMessage(e.getMessage());
            err.setPath(request.getRequestURI());
            return ResponseEntity.status(status.value()).body(err);
    }

    @ExceptionHandler(DataBaseExcptions.class)
    public ResponseEntity<StandardError> dataBaseExcption(DataBaseExcptions e, HttpServletRequest request){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Database Excption");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(err);
    }
}
