package com.healthcare.finder.doctorHospitalFinder.application.classException;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HospitalApplicationException extends RuntimeException {
    final private HttpStatus httpStatus;
    public HospitalApplicationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
