package com.timoxino.interview.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = ObjectNotFoundException.message)
public class ObjectNotFoundException extends RuntimeException {
    public final static String message = "Object was not found by 'id' provided.";
}
