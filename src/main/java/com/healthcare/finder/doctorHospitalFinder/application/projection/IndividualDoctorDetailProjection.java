package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndividualDoctorDetailProjection {
    private String doctorName;
    private int doctorAge;
    private String doctorGender;
    private int doctorYearsOfExperience;
    private double doctorRating;
    private String doctorGraduateCollege;
    private String doctorFieldOfExpertise;
    private String doctorType;
}
