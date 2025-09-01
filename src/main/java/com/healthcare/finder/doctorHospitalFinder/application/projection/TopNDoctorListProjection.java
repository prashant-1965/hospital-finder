package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopNDoctorListProjection {
    private String doctorName;
    private int yearOfExp;
    private double doctorRatting;
    private String doctorDetailAddress;
}
