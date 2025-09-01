package com.healthcare.finder.doctorHospitalFinder.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRegistrationDto{
    private String doctorName;
    private String hospitalName;
    private String facilityName;
    private LocalDateTime localDateTime;
}