package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalReviewDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;

import java.util.List;

public interface HospitalService {
    List<TopNHospitalListProjection> getTopNHospitalListByCountry(String countryName, int n);
    List<TopNHospitalListProjection> getTopNHospitalList();
    List<TopNHospitalListProjection> getTopNGovHospitalList(String countryName, String specializedIn);
    List<TopNHospitalListProjection> getTopNPrivateHospitalList(String countryName, String specializedIn);
    String addHospitalReviews(HospitalReviewDto hospitalReviewDto);
    String registerHospital(HospitalRegisterDto hospitalRegisterDto);
    List<String> findAllAvailableHospital();
}
