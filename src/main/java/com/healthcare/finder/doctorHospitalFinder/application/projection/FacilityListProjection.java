package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacilityListProjection implements Comparable<FacilityListProjection> {
    private String facilityName;
    private String facilityDescription;
    @Override
    public int compareTo(FacilityListProjection f) {
        return this.facilityName.compareTo(f.facilityName);
    }
}
