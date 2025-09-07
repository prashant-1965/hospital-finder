package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class HospitalApplicationProjection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
