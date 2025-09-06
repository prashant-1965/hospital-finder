package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.*;
import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalReviewDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.*;
import com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualHospitalDetailProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;
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
    @Autowired
    private HospitalApplicationRepo hospitalApplicationRepo;

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
        Optional<AppUser> appUser = appUserRepo.findByUserEmail(hospitalReviewDto.getAppUserEmail());
        if(appUser.isEmpty()){
            throw new HospitalException("Please register your "+hospitalReviewDto.getAppUserEmail()+"!",HttpStatus.BAD_REQUEST);
        }
        Hospital hospital = hospitalRepo.findByHospitalName(hospitalReviewDto.getHospitalName());
        long totalReview = hospitalReviewRepo.getTotalReviewForIndividualHospitalByName(hospitalReviewDto.getHospitalName());
        double previousRatting = hospitalRepo.getRattingByHospitalName(hospitalReviewDto.getHospitalName());
        double newAvgRatting = ((previousRatting * totalReview) + hospitalReviewDto.getHospitalRating()) / (totalReview + 1);
        newAvgRatting = Math.round(newAvgRatting * 10.0) / 10.0;
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

    @Transactional
    @Override
    public String registerHospital(String hospitalName) throws HospitalException, CountryException, StateException, FacilitiesException {

        HospitalApplication hospitalApplication = hospitalApplicationRepo.getByHospitalName(hospitalName);
        Country country = countryRepo.findCountryByName(hospitalApplication.getTempCountryName());

        State state = statesRepo.findByStateName(hospitalApplication.getTempStateName());

        List<MedicalFacilities> medicalFacilitiesList = new ArrayList<>(hospitalApplication.getFacilities());

        Hospital hospital = new Hospital();
        hospital.setHospitalName(hospitalApplication.getTempHospitalName());
        hospital.setHospitalType(hospitalApplication.getTempHospitalType());
        hospital.setHospitalYearOfEstablishment(hospitalApplication.getTempHospitalYearOfEstablishment());
        hospital.setHospitalNumOfUsersServed(hospitalApplication.getTempHospitalNumOfUsersServed());
        hospital.setHospitalContact(hospitalApplication.getTempHospitalContact());
        hospital.setHospitalAddress(hospitalApplication.getTempHospitalAddress());
        hospital.setCountry(country);
        hospital.setState(state);
        hospital.setFacilities(medicalFacilitiesList);
        hospitalRepo.save(hospital);
        hospitalApplicationRepo.deleteHospitalApplicationByName(hospitalName);
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

    @Override
    public String findHospitalByDoctoEmail(String doctorEmail) throws HospitalException {
        Optional<String> hospital = hospitalRepo.getHospitalByDoctorEmail(doctorEmail);
        if(hospital.isEmpty()){
            throw new HospitalException("Doctor with email "+doctorEmail+" is not working in any Hospital",HttpStatus.NOT_FOUND);
        }
        return hospital.get();
    }

    @Override
    public IndividualHospitalDetailProjection findHospitalDetailByName(String hospitalName) throws HospitalException {
        Optional<IndividualHospitalDetailProjection> individualHospitalDetailProjection = hospitalRepo.getHospitalDetailByName(hospitalName);
        if(individualHospitalDetailProjection.isEmpty()){
            throw new HospitalException("No hospital found with name: "+hospitalName,HttpStatus.NOT_FOUND);
        }
        return individualHospitalDetailProjection.get();
    }

    @Override
    public List<String> findHospitalByFacilityName(String facilityName) throws HospitalException{
        List<String> hospitalList = hospitalRepo.getHospitalByFacilityName(facilityName);
        if(hospitalList.isEmpty()){
            throw new HospitalException(facilityName+" is not available in any Hospital",HttpStatus.NOT_FOUND);
        }
        return hospitalList;
    }

    @Override
    public List<String> findHospitalByFacilityNameAndDoctorEmail(String facilityName, String doctorEmail) throws HospitalException {
        List<String> hospitalList = hospitalRepo.getHospitalByFacilityNameAndDoctorEmail(facilityName,doctorEmail);
        if(hospitalList.isEmpty()){
            throw new HospitalException("Currently No doctor is providing in "+facilityName+" facility!",HttpStatus.NOT_FOUND);
        }
        return hospitalList;
    }
}
