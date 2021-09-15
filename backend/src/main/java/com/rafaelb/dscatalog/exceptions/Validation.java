package com.rafaelb.dscatalog.exceptions;

import com.rafaelb.dscatalog.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

public class Validation extends Throwable {

    public Validation() {
    }
}
