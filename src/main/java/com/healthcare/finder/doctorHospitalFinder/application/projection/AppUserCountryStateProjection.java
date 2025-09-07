package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class AppUserCountryStateProjection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String stateName;
    private String countryName;
}
