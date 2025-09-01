package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUserCountryStateProjection {
    private String stateName;
    private String countryName;
}
