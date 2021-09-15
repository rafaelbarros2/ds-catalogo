package com.rafaelb.dscatalog.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class ConstraintViolationException extends RuntimeException {

    private Set<String> errors;

    public ConstraintViolationException(String msg) {
        super(msg);
    }
}
