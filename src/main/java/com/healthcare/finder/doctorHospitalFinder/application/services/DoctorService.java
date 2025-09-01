package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.DoctorRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.dto.DoctorReviewDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.Doctor;
import com.healthcare.finder.doctorHospitalFinder.application.entity.DoctorApplication;
import com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualDoctorDetailProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection;

import java.util.List;

public interface DoctorService {
    List<TopNDoctorListProjection> getTopNDoctorListByCountry(String countryName, int n);
    List<TopNDoctorListProjection> getTop10DoctorList();
    List<TopNDoctorListProjection> getTop5DoctorListByCountry(String countryName);
    List<TopNDoctorListProjection> getTopNGovDoctorBySpecialisationList(String countryName, String specializedIn);
    List<TopNDoctorListProjection> getTopNPrivateDoctorBySpecialisationList(String countryName, String specializedIn);
    List<TopNDoctorListProjection> getNDoctorByExperienceAndSpecialisationList(String countryName, String specializedIn, String type, String order);
    String addDoctor(DoctorApplication doctorApplication);
    IndividualDoctorDetailProjection findDoctorDetailByName(String name);
    String addDoctorReviews(DoctorReviewDto doctorReviewDto);
    List<String> findAllAvailableDoctor();
}
