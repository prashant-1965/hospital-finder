package com.healthcare.finder.doctorHospitalFinder.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class HospitalReviewDto {
    private String appUserName;
    private String hospitalName;
    private double hospitalRating;
    private String comments;
}
