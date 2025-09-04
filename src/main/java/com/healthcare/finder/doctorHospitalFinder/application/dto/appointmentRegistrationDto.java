package com.healthcare.finder.doctorHospitalFinder.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class appointmentRegistrationDto {
    private String appUserEmail;
    private String facilityName;
    private String hospitalName;
    private String doctorName;
    private LocalDateTime localDateTime; // provide calendar and time option
}