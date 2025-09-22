package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.HospitalApplication;
import com.healthcare.finder.doctorHospitalFinder.application.projection.HospitalApplicationProjection;

import java.util.List;

public interface HospitalApplicationService {
    String addHospitalRegistrationRequest(HospitalRegisterDto hospitalRegisterDto);
    List<HospitalApplicationProjection> findAllPendingHospitalRequest();
    String hospitalRemovalRequest(String hospitalName);
    HospitalApplication getByHospitalName(String hospitalName);
}
