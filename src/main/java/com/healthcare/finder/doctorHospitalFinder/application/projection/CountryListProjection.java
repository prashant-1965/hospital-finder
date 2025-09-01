package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryListProjection implements Comparable<CountryListProjection> {
    private String countryName;
    @Override
    public int compareTo(CountryListProjection o) {
        return this.countryName.compareTo(o.countryName);
    }
}