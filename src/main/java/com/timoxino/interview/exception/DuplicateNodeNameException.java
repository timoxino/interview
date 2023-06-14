package com.timoxino.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = DuplicateNodeNameException.message)
public class DuplicateNodeNameException extends Exception {
    public final static String message = "Node with such 'name' attribute value already exists.";
}
