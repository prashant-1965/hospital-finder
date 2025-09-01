package com.healthcare.finder.doctorHospitalFinder.application.dto;

import lombok.Data;

@Data
public class DoctorReviewDto {
    private String userName;
    private String doctorName;
    private double doctorRatting;
    private String comment;
}
