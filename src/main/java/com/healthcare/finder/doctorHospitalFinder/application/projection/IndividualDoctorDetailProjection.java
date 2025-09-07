package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class IndividualDoctorDetailProjection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String doctorName;
    private int doctorAge;
    private String doctorGender;
    private String doctorEmail;
    private int doctorYearsOfExperience;
    private double doctorRating;
    private String doctorGraduateCollege;
    private String doctorFieldOfExpertise;
    private String doctorType;
}
