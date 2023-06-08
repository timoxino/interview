package com.timoxino.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = ParentDetailsMissingException.message)
public class ParentDetailsMissingException extends Exception {
    public final static String message = "Parent object details ('name' or 'id') must present in the request.";
}
