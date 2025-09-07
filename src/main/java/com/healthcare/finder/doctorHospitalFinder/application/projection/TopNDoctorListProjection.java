package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class TopNDoctorListProjection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String doctorName;
    private int yearOfExp;
    private double doctorRatting;
    private String doctorDetailAddress;
}
