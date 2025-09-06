package com.healthcare.finder.doctorHospitalFinder.application.globalException;

import com.healthcare.finder.doctorHospitalFinder.application.classException.*;
import com.healthcare.finder.doctorHospitalFinder.application.entity.DoctorApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalClassException{

    @ExceptionHandler(AppUserException.class)
    public ResponseEntity<String> checkValidUser(AppUserException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(DoctorsException.class)
    public ResponseEntity<String> validateDoctor(DoctorsException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(FacilitiesException.class)
    public ResponseEntity<String> validateFacility(FacilitiesException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(GlobalReviewException.class)
    public ResponseEntity<String> validateReview(GlobalReviewException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(CountryException.class)
    public ResponseEntity<String> validateCountry(CountryException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(StateException.class)
    public ResponseEntity<String> validateState(StateException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(HospitalException.class)
    public ResponseEntity<String> validateHospital(HospitalException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(DoctorApplicationException.class)
    public ResponseEntity<String> validateDoctorApplication(DoctorApplicationException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(HospitalApplicationException.class)
    public ResponseEntity<String> validateHospitalApplication(HospitalApplicationException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(AppointmentException.class)
    public ResponseEntity<String> validateAppointment(AppointmentException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
