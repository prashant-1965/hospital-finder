package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HospitalApplicationProjection {
    private String tempHospitalName;
    private String tempHospitalType;
    private int tempHospitalYearOfEstablishment;
    private int tempHospitalNumOfUsersServed;
    private double tempHospitalRating;
    private String tempHospitalContact;
    private String tempHospitalAddress;
    private String tempCountryName;
    private String tempStateName;
    private List<String> medicalFacilities;
}
