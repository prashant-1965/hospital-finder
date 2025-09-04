package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.MedicalFacilitiesRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FacilitiesService {
    List<FacilityListProjection> getAllAvailableFacilities();
    String addFacility(MedicalFacilitiesRegisterDto medicalFacilitiesRegisterDto);
    List<String> findFacilityByDoctorEmail(String doctorEmail);
    List<String> findFacilityByHospitalName(String hospitalName);
}
