package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StateListProjection implements Comparable<StateListProjection> {
    private String stateName;
    @Override
    public int compareTo(StateListProjection s) {
        return this.stateName.compareTo(s.stateName);
    }
}
