package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class CountryListProjection implements Comparable<CountryListProjection>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String countryName;
    @Override
    public int compareTo(CountryListProjection o) {
        return this.countryName.compareTo(o.countryName);
    }
}