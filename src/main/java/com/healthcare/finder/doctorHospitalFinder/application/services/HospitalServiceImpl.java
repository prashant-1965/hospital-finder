package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.*;
import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalReviewDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.*;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private HospitalReviewRepo hospitalReviewRepo;
    @Autowired
    private CountryRepo countryRepo;
    @Autowired
    private StatesRepo statesRepo;
    @Autowired
    private FacilitiesRepo facilitiesRepo;
    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    public List<TopNHospitalListProjection> getTopNHospitalListByCountry(String countryName, int n) throws HospitalException {
        if(countryName.isEmpty()){
            throw new HospitalException("Please provide a valid country name", HttpStatus.BAD_REQUEST);
        }
        List<TopNHospitalListProjection> topNHospitalListProjections = hospitalRepo.getFirstNHospitalListByCountry(countryName,PageRequest.of(0,n));
        if(topNHospitalListProjections.isEmpty()){
            throw new HospitalException("No Hospital found in "+countryName,HttpStatus.NOT_FOUND);
        }
        return topNHospitalListProjections;
    }

    @Override
    public List<TopNHospitalListProjection> getTopNHospitalList() throws HospitalException {
        List<TopNHospitalListProjection> topNHospitalListProjections = hospitalRepo.getFirstNHospitalList(PageRequest.of(0,10));
        if(topNHospitalListProjections.isEmpty()){
            throw new HospitalException("No Hospital found",HttpStatus.NOT_FOUND);
        }
        return topNHospitalListProjections;
    }

    @Override
    public List<TopNHospitalListProjection> getTopNGovHospitalList(String countryName, String specializedIn) throws HospitalException {
        if (countryName.isEmpty()) {
            throw new HospitalException("Please provide a valid country name", HttpStatus.BAD_REQUEST);
        }
        if (specializedIn.isEmpty()) {
            throw new HospitalException("Please provide at least one specialisation", HttpStatus.BAD_REQUEST);
        }
        List<TopNHospitalListProjection> topNHospitalListProjections = hospitalRepo.getFirstNGovHospitalList(countryName, specializedIn);
        if(topNHospitalListProjections.isEmpty()){
            throw new HospitalException("No Hospital found in "+countryName+" or in Specialisation"+specializedIn,HttpStatus.NOT_FOUND);
        }
        return topNHospitalListProjections;
    }

    @Override
    public List<TopNHospitalListProjection> getTopNPrivateHospitalList(String countryName, String specializedIn) throws HospitalException {
        if(countryName.isEmpty()){
            throw new HospitalException("Please provide a valid country name", HttpStatus.BAD_REQUEST);
        }
        if(specializedIn.isEmpty()){
            throw new HospitalException("Please provide at least one specialisation", HttpStatus.BAD_REQUEST);
        }
        List<TopNHospitalListProjection> topNHospitalListProjections = hospitalRepo.getFirstNPrivateHospitalList(countryName,specializedIn);
        if(topNHospitalListProjections.isEmpty()){
            throw new HospitalException("No Hospital found in "+countryName+" or in Specialisation"+specializedIn,HttpStatus.NOT_FOUND);
        }
        return topNHospitalListProjections;
    }

    @Override
    public String addHospitalReviews(HospitalReviewDto hospitalReviewDto) throws HospitalException{
        Optional<AppUser> appUser = appUserRepo.findByUsername(hospitalReviewDto.getAppUserName());
        if(appUser.isEmpty()){
            throw new HospitalException("Please register your self!",HttpStatus.BAD_REQUEST);
        }
        Hospital hospital = hospitalRepo.findByHospitalName(hospitalReviewDto.getHospitalName());
        long totalReview = hospitalReviewRepo.getTotalReviewForIndividualHospitalByName(hospitalReviewDto.getHospitalName());
        double previousRatting = hospitalRepo.getRattingByHospitalName(hospitalReviewDto.getHospitalName());
        double newAvgRatting = ((previousRatting * totalReview) + hospitalReviewDto.getHospitalRating()) / (totalReview + 1);
        hospital.setHospitalRating(newAvgRatting);
        HospitalReview hospitalReview = new HospitalReview();
        hospitalReview.setRating(hospitalReviewDto.getHospitalRating());
        hospitalReview.setComments(hospitalReviewDto.getComments());
        hospitalReview.setCreatedAt(LocalDateTime.now());
        hospitalReview.setHospital(hospital);
        hospitalReview.setUser(appUser.get());
        hospitalReviewRepo.save(hospitalReview);
        return "Review Added SuccessFully!";
    }

    @Override
    public String registerHospital(HospitalRegisterDto hospitalRegisterDto) throws HospitalException, CountryException, StateException, FacilitiesException {
        Country country = countryRepo.findCountryByName(hospitalRegisterDto.getCountryName());
        if(country==null){
            throw new CountryException("Service is not available in "+hospitalRegisterDto.getCountryName(),HttpStatus.NOT_FOUND);
        }
        State state = statesRepo.findByStateName(hospitalRegisterDto.getStateName());
        if(state==null){
            throw new StateException("Our service is not available in "+hospitalRegisterDto.getStateName(),HttpStatus.NOT_FOUND);
        }
        List<MedicalFacilities> medicalFacilitiesList = new ArrayList<>();
        for(String facility:hospitalRegisterDto.getFacilities()){
            MedicalFacilities medicalFacilities = facilitiesRepo.findByFacilityName(facility);
            if(medicalFacilities==null){
                throw new FacilitiesException(facility+" facility is not Available",HttpStatus.BAD_REQUEST);
            }
            medicalFacilitiesList.add(medicalFacilities);
        }
        Hospital existHospital = hospitalRepo.findByHospitalName(hospitalRegisterDto.getHospitalName());
        if(existHospital!=null){
            throw new HospitalException(hospitalRegisterDto.getHospitalName()+" already exist!",HttpStatus.BAD_REQUEST);
        }
        Hospital hospital = new Hospital();
        hospital.setHospitalName(hospitalRegisterDto.getHospitalName());
        hospital.setHospitalType(hospitalRegisterDto.getHospitalType());
        hospital.setHospitalYearOfEstablishment(hospitalRegisterDto.getHospitalYearOfEstablishment());
        hospital.setHospitalNumOfUsersServed(hospitalRegisterDto.getHospitalNumOfUsersServed());
        hospital.setHospitalContact(hospitalRegisterDto.getHospitalContact());
        hospital.setHospitalAddress(hospitalRegisterDto.getHospitalAddress());
        hospital.setCountry(country);
        hospital.setState(state);
        hospital.setFacilities(medicalFacilitiesList);
        hospitalRepo.save(hospital);
        return "Hospital registered Successfully";
    }

    @Override
    public List<String> findAllAvailableHospital() throws DoctorsException{
        List<String> hospitalList = hospitalRepo.findAllHospital();
        if(hospitalList.isEmpty()){
            throw new DoctorsException("No doctor available!",HttpStatus.NOT_FOUND);
        }
        return hospitalList;
    }
}
