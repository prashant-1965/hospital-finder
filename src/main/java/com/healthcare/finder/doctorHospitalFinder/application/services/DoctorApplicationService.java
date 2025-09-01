package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.DoctorRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.DoctorApplication;

import java.util.List;

public interface DoctorApplicationService {
    String addDoctorApplicationRequest(DoctorRegisterDto doctorRegisterDto);
    List<DoctorApplication> findAllPendingDoctors();
}
