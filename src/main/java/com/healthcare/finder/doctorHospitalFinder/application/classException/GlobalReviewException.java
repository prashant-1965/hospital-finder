package com.healthcare.finder.doctorHospitalFinder.application.classException;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalReviewException extends RuntimeException {
    final private HttpStatus httpStatus;
    public GlobalReviewException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
