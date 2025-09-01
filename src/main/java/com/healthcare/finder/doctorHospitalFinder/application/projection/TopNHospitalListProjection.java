package com.healthcare.finder.doctorHospitalFinder.application.projection;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopNHospitalListProjection {
    private String hospitalName;
    private String hospitalType;
    private double hospitalRating;
    private String hospitalAddress;
}
