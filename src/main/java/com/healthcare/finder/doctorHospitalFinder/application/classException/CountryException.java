package com.healthcare.finder.doctorHospitalFinder.application.classException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class CountryException extends RuntimeException {
    final private HttpStatus httpStatus;
    public CountryException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
