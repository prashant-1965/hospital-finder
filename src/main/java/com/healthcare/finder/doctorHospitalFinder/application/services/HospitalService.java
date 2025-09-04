package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalReviewDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualHospitalDetailProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;

import java.util.List;

public interface HospitalService {
    List<TopNHospitalListProjection> getTopNHospitalListByCountry(String countryName, int n);
    List<TopNHospitalListProjection> getTopNHospitalList();
    List<TopNHospitalListProjection> getTopNGovHospitalList(String countryName, String specializedIn);
    List<TopNHospitalListProjection> getTopNPrivateHospitalList(String countryName, String specializedIn);
    String addHospitalReviews(HospitalReviewDto hospitalReviewDto);
    String registerHospital(String hospitalName);
    List<String> findAllAvailableHospital();
    String findHospitalByDoctoEmail(String doctorEmail);
    IndividualHospitalDetailProjection findHospitalDetailByName(String hospitalName);
    List<String> findHospitalByFacilityName(String facilityName);
    List<String> findHospitalByFacilityNameAndDoctorEmail(String facilityName,String doctorEmail);
}
