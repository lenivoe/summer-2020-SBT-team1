package com.summer.gateway.remote.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ApiBad extends RuntimeException {
    public ApiBad(String message) {
        super(message);
    }
}
