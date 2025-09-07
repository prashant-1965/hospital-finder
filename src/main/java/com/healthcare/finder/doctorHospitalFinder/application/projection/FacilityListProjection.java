package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class FacilityListProjection implements Comparable<FacilityListProjection>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String facilityName;
    private String facilityDescription;
    @Override
    public int compareTo(FacilityListProjection f) {
        return this.facilityName.compareTo(f.facilityName);
    }
}
