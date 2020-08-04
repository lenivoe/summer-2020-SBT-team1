package com.summer.gateway.remote.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmptyField extends RuntimeException {
    public EmptyField(String nameFiled) {
        super("Field " + nameFiled + " is empty");
    }
}
