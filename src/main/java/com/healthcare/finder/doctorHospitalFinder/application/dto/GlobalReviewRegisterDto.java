package com.healthcare.finder.doctorHospitalFinder.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GlobalReviewRegisterDto {
    private String appUserName;
    private double rating;
    private String comments;
}
