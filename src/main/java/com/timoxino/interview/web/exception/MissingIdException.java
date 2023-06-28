package com.timoxino.interview.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = MissingIdException.message)
public class MissingIdException extends Exception {
    public final static String message = "Object's 'id' attribute can not be null.";
}
