package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.*;
import com.healthcare.finder.doctorHospitalFinder.application.dto.DoctorReviewDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.*;
import com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualDoctorDetailProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService{

    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private CountryRepo countryRepo;
    @Autowired
    private StatesRepo statesRepo;
    @Autowired
    private FacilitiesRepo facilitiesRepo;
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private DoctorReviewRepo doctorReviewRepo;
    @Autowired
    private AppUserServices appUserServices;
    @Autowired
    private DoctorApplicationRepo doctorApplicationRepo;


    @Override
    public List<TopNDoctorListProjection> getTopNDoctorListByCountry(String countryName, int n) throws DoctorsException {
        if(countryName.isEmpty()){
            throw new DoctorsException("Please provide a valid country name", HttpStatus.BAD_REQUEST);
        }
        if(n==0){
            throw new DoctorsException("Please chose a number", HttpStatus.BAD_REQUEST);
        }
        List<TopNDoctorListProjection>  topNDoctorListProjections = doctorRepo.findTopNDoctorsByCountry(countryName, PageRequest.of(0, n));
        if(topNDoctorListProjections.isEmpty()){
            throw new DoctorsException("No data found",HttpStatus.NOT_FOUND);
        }
        return topNDoctorListProjections;
    }

    @Override
    public List<TopNDoctorListProjection> getTop10DoctorList() throws DoctorsException {

        List<TopNDoctorListProjection>  topNDoctorListProjections = doctorRepo.findTop10Doctors(PageRequest.of(0, 10));
        if(topNDoctorListProjections.isEmpty()){
            throw new DoctorsException("No data found",HttpStatus.NOT_FOUND);
        }
        return topNDoctorListProjections;
    }

    @Override
    public List<TopNDoctorListProjection> getTop5DoctorListByCountry(String countryName) {
        List<TopNDoctorListProjection>  topNDoctorListProjections = doctorRepo.findTopNDoctorsByCountry(countryName,PageRequest.of(0, 5));
        if(topNDoctorListProjections.isEmpty()){
            throw new DoctorsException("No data found",HttpStatus.NOT_FOUND);
        }
        return topNDoctorListProjections;
    }

    @Override
    public List<TopNDoctorListProjection> getTopNGovDoctorBySpecialisationList(String countryName, String specializedIn) throws DoctorsException{
        if(countryName.isEmpty()){
            throw new DoctorsException("Please provide a valid country name", HttpStatus.BAD_REQUEST);
        }
        if(specializedIn.isEmpty()){
            throw new DoctorsException("Please provide at least one specialisation", HttpStatus.BAD_REQUEST);
        }
        List<TopNDoctorListProjection>  topNDoctorListProjections = doctorRepo.findTopNGovDoctorsAsFacility(countryName, specializedIn);
        if(topNDoctorListProjections.isEmpty()){
            throw new DoctorsException("No data found",HttpStatus.NOT_FOUND);
        }
        return topNDoctorListProjections;
    }

    @Override
    public List<TopNDoctorListProjection> getTopNPrivateDoctorBySpecialisationList(String countryName, String specializedIn) throws DoctorsException  {
        if(countryName.isEmpty()){
            throw new DoctorsException("Please provide a valid country name", HttpStatus.BAD_REQUEST);
        }
        if(specializedIn.isEmpty()){
            throw new DoctorsException("Please provide at least one specialisation", HttpStatus.BAD_REQUEST);
        }
        List<TopNDoctorListProjection>  topNDoctorListProjections = doctorRepo.findTopNPrivateDoctorsWithFacility(countryName, specializedIn);
        if(topNDoctorListProjections.isEmpty()){
            throw new DoctorsException("No data found",HttpStatus.NOT_FOUND);
        }
        return topNDoctorListProjections;
    }

    @Override
    public List<TopNDoctorListProjection> getNDoctorByExperienceAndSpecialisationList(String countryName, String specializedIn, String type, String order) throws DoctorsException  {
        if(countryName.isEmpty()){
            throw new DoctorsException("Please provide a valid country name", HttpStatus.BAD_REQUEST);
        }
        if(specializedIn.isEmpty()){
            throw new DoctorsException("Please provide at least one specialisation", HttpStatus.BAD_REQUEST);
        }
        if(type.isEmpty()){
            throw new DoctorsException("Please provide a valid type name", HttpStatus.BAD_REQUEST);
        }
        if(order.isEmpty()){
            order = "desc";
        }
        List<TopNDoctorListProjection>  topNDoctorListProjections;
        if(order.equals("gov") && type.equals("desc")) {
            topNDoctorListProjections = doctorRepo.findTopNGovExperiencedDoctorWithSpecialisation(countryName,specializedIn);
        }else if(order.equals("gov") && type.equals("inc")) {
            topNDoctorListProjections = doctorRepo.findLeastNGovExperiencedDoctorWithSpecialisation(countryName,specializedIn);
        }else if(order.equals("private") && type.equals("desc")) {
            topNDoctorListProjections = doctorRepo.findTopNPrivateExperiencedDoctorWithSpecialisation(countryName,specializedIn);
        }else {
            topNDoctorListProjections = doctorRepo.findLeastNPrivateExperiencedDoctorWithSpecialisation(countryName, specializedIn);
        }
        if(topNDoctorListProjections.isEmpty()){
            throw new DoctorsException("No data found",HttpStatus.NOT_FOUND);
        }
        return topNDoctorListProjections;
    }

    @Override
    @Transactional
    public String addDoctor(DoctorApplication doctorApplication) throws CountryException,StateException,AppUserException {
        Country country = countryRepo.findCountryByName(doctorApplication.getTmpDoctorCountryName());

        State state = statesRepo.findByStateName(doctorApplication.getTmpDoctorStateName());

        List<MedicalFacilities> medicalFacilitiesList = new ArrayList<>(doctorApplication.getMedicalFacilities());

        Hospital hospital = hospitalRepo.findByHospitalName(doctorApplication.getHospitalAppliedFor());
        Doctor doctor = new Doctor();
        doctor.setDoctorName(doctorApplication.getTmpDoctorName());
        doctor.setDoctorAge(doctorApplication.getTmpDoctorAge());
        doctor.setDoctorGender(doctorApplication.getTmpDoctorGender());
        doctor.setDoctorYearsOfExperience(doctorApplication.getTmpDoctorYearsOfExperience());
        doctor.setDoctorGraduateCollege(doctorApplication.getTmpDoctorGraduateCollege());
        doctor.setDoctorFieldOfExpertise(doctorApplication.getTmpDoctorFieldOfExpertise());
        doctor.setDoctorEmail(doctorApplication.getTmpDoctorEmail());
        doctor.setDoctorDetailAddress(doctorApplication.getTmpDoctorDetailAddress());
        doctor.setDoctorType(doctorApplication.getTmpDoctorType());
        doctor.setCountry(country);
        doctor.setState(state);
        doctor.setHospital(hospital);
        doctor.setFacilities(medicalFacilitiesList);
        doctorRepo.save(doctor);
        doctorApplicationRepo.deleteDoctorApplicationByEmail(doctorApplication.getTmpDoctorEmail());
        return doctorApplication.getTmpDoctorName() + " has appointed in " + doctorApplication.getHospitalAppliedFor();
    }

    @Override
    public IndividualDoctorDetailProjection findDoctorDetailByName(String doctorName) {
        return doctorRepo.findDetailsByDoctorName(doctorName);
    }

    @Override
    public String addDoctorReviews(DoctorReviewDto doctorReviewDto) throws DoctorsException {
        Optional<AppUser> appUser = appUserRepo.findByUserEmail(doctorReviewDto.getUserEmail());
        if(appUser.isEmpty()){
            throw new DoctorsException("Please register your "+doctorReviewDto.getUserEmail()+"!",HttpStatus.BAD_REQUEST);
        }
        Doctor doctor = doctorRepo.findByDoctorName(doctorReviewDto.getDoctorName());
        long totalReview = doctorReviewRepo.getTotalReviewForIndividualDoctorByName(doctorReviewDto.getDoctorName());
        double previousRatting = doctorRepo.getAvgRattingByDoctorName(doctorReviewDto.getDoctorName());
        double newAvgRatting = ((previousRatting * totalReview) + doctorReviewDto.getDoctorRatting()) / (totalReview + 1);
        newAvgRatting = Math.round(newAvgRatting * 10.0) / 10.0;
        doctor.setDoctorRating(newAvgRatting);
        DoctorReview doctorReview = new DoctorReview();
        doctorReview.setRating(doctorReviewDto.getDoctorRatting());
        doctorReview.setComments(doctorReviewDto.getComment());
        doctorReview.setCreatedAt(LocalDateTime.now());
        doctorReview.setDoctor(doctor);
        doctorReview.setUser(appUser.get());
        doctorReviewRepo.save(doctorReview);
        return "Review Added Successfully";
    }

    @Override
    public List<String> findAllAvailableDoctor() throws DoctorsException {
        List<String> doctorList = doctorRepo.findAllDoctors();
        if(doctorList.isEmpty()){
            throw new DoctorsException("No doctor available!",HttpStatus.NOT_FOUND);
        }
        return doctorList;
    }

    @Override
    public List<String> findDoctorByFacilityAndHospital(String hospitalName, String facilityName) {
        List<String> doctorList = doctorRepo.getDoctorByFacilityAndHospital(hospitalName,facilityName);
        if(doctorList.isEmpty()){
            throw new DoctorsException("We don't have any doctor in "+hospitalName+" with "+facilityName+" facility",HttpStatus.NOT_FOUND);
        }
        return doctorList;
    }
}
