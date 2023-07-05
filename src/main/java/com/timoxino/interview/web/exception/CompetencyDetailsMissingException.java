package com.timoxino.interview.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = CompetencyDetailsMissingException.message)
public class CompetencyDetailsMissingException extends Exception {
    public final static String message = "Object reference does not have 'id' specified.";
}
