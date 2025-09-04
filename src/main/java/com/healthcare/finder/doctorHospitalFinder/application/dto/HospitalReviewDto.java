package com.healthcare.finder.doctorHospitalFinder.application.dto;

import lombok.Data;

@Data
public class HospitalReviewDto {
    private String appUserEmail;
    private String hospitalName;
    private double hospitalRating;
    private String comments;
}
