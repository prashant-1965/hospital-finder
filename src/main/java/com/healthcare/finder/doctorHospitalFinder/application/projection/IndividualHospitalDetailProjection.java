package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndividualHospitalDetailProjection {
    private String hospitalName;
    private String hospitalType;
    private int hospitalYearOfEstablishment;
    private int hospitalNumOfUsersServed;
    private double hospitalRating;
    private String hospitalContact;
    private String hospitalAddress;
}
