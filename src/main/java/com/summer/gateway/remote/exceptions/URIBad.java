package com.summer.gateway.remote.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class URIBad extends RuntimeException {
    public URIBad(String message) {
        super(message);
    }
}
