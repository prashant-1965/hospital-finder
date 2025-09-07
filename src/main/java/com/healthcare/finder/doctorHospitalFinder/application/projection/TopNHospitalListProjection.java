package com.healthcare.finder.doctorHospitalFinder.application.projection;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class TopNHospitalListProjection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String hospitalName;
    private String hospitalType;
    private double hospitalRating;
    private String hospitalAddress;
}
