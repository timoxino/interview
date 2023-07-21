package com.timoxino.interview.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = MissingIdException.message)
public class MissingIdException extends Exception {
    public final static String message = "Object's 'uuid' attribute can not be null.";
}
