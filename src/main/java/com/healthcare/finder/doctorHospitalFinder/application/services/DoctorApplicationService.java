package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.DoctorRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.DoctorApplicationProjection;

import java.util.List;

public interface DoctorApplicationService {
    String addDoctorApplicationRequest(DoctorRegisterDto doctorRegisterDto);
    List<DoctorApplicationProjection> findAllPendingDoctors();
    String removeDoctorByEmail(String email);
}
