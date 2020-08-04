package com.summer.gateway.remote.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class InstanceNotFound extends RuntimeException {
    public InstanceNotFound(String uuid) {
        super("Instance with instance_id: " + uuid + " not found");
    }
}
