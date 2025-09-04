package com.healthcare.finder.doctorHospitalFinder.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class DoctorRegisterDto {
    private String doctorName;
    private int doctorAge;
    private String doctorGender;
    private int doctorYearsOfExperience;
    private String doctorGraduateCollege;
    private String doctorFieldOfExpertise;
    private String doctorEmail;
    private String doctorMobile;
    private String doctorDetailAddress;
    private String doctorType; // gov or private drop down
    private String hospitalAppliedFor; // drop down for hospital list
    private String countryName;
    private String stateName;
    private List<String> facilityNames;
}
