package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.StateRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.State;
import com.healthcare.finder.doctorHospitalFinder.application.projection.StateListProjection;

import java.util.List;

public interface StateService {
    List<StateListProjection> getStateList( String countryName);
    String addState(StateRegisterDto stateRegisterDto);
    State findByStateName(String stateName);
}
