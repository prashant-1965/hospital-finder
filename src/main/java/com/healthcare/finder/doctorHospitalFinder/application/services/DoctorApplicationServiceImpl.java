package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.*;
import com.healthcare.finder.doctorHospitalFinder.application.dto.AppUserRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.dto.DoctorRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.*;
import com.healthcare.finder.doctorHospitalFinder.application.projection.DoctorApplicationProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.font.OpenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DoctorApplicationServiceImpl implements DoctorApplicationService{
    @Autowired
    private DoctorApplicationRepo doctorApplicationRepo;
    @Autowired
    private FacilitiesRepo facilitiesRepo;
    @Autowired
    private CountryRepo countryRepo;
    @Autowired
    private StatesRepo statesRepo;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private AppUserServices appUserServices;

    @Override
    @Transactional
    @CacheEvict(value = "AllPendingDoctors",allEntries = true)
    public String addDoctorApplicationRequest(DoctorRegisterDto doctorRegisterDto)  throws CountryException, StateException, AppUserException {

        Country country = countryRepo.findCountryByName(doctorRegisterDto.getCountryName());
        if(country==null){
            throw new CountryException("We are not authorized for work in "+doctorRegisterDto.getCountryName(),HttpStatus.NOT_FOUND);
        }
        State state = statesRepo.findByStateName(doctorRegisterDto.getStateName());
        if(state==null){
            throw new StateException("We are not authorized for work in "+doctorRegisterDto.getStateName(),HttpStatus.NOT_FOUND);
        }

        List<MedicalFacilities> medicalFacilitiesList = new ArrayList<>();
        for(String facility:doctorRegisterDto.getFacilityNames()){
            MedicalFacilities medicalFacilities = facilitiesRepo.findByFacilityName(facility);
            if(medicalFacilities==null){
                throw new FacilitiesException(facility+" facility is not Available", HttpStatus.BAD_REQUEST);
            }
            medicalFacilitiesList.add(medicalFacilities);
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        DoctorApplication doctorApplication = new DoctorApplication();
        doctorApplication.setTmpDoctorName(doctorRegisterDto.getDoctorName());
        doctorApplication.setTmpDoctorEmail(doctorRegisterDto.getDoctorEmail());
        doctorApplication.setTmpDoctorAge(doctorRegisterDto.getDoctorAge());
        doctorApplication.setTmpDoctorGender(doctorRegisterDto.getDoctorGender());
        doctorApplication.setTmpDoctorMobile(doctorRegisterDto.getDoctorMobile());
        doctorApplication.setHospitalAppliedFor(doctorRegisterDto.getHospitalAppliedFor());
        doctorApplication.setTmpDoctorType(doctorRegisterDto.getDoctorType());
        doctorApplication.setMedicalFacilities(medicalFacilitiesList);
        doctorApplication.setTmpDoctorDetailAddress(doctorRegisterDto.getDoctorDetailAddress());
        doctorApplication.setTmpDoctorFieldOfExpertise(doctorRegisterDto.getDoctorFieldOfExpertise());
        doctorApplication.setTmpDoctorYearsOfExperience(doctorRegisterDto.getDoctorYearsOfExperience());
        doctorApplication.setTmpDoctorGraduateCollege(doctorRegisterDto.getDoctorGraduateCollege());
        doctorApplication.setTmpDoctorStateName(doctorRegisterDto.getStateName());
        doctorApplication.setTmpDoctorCountryName(doctorRegisterDto.getCountryName());
        doctorApplicationRepo.save(doctorApplication);


        AppUserRegisterDto appUserRegisterDto = new AppUserRegisterDto();
        appUserRegisterDto.setUserName(doctorRegisterDto.getDoctorName());
        appUserRegisterDto.setUserAge(doctorRegisterDto.getDoctorAge());
        appUserRegisterDto.setUserEmail(doctorRegisterDto.getDoctorEmail());
        appUserRegisterDto.setUserCountry(doctorRegisterDto.getCountryName());
        appUserRegisterDto.setUserState(doctorRegisterDto.getStateName());
        appUserRegisterDto.setUserGender(doctorRegisterDto.getDoctorGender());
        appUserRegisterDto.setUserMobile(doctorRegisterDto.getDoctorMobile());
        appUserRegisterDto.setRole("DOCTOR");
        appUserRegisterDto.setPassword(password.toString());
        String message = appUserServices.addAppUser(appUserRegisterDto);
        return "Your Request sent Successfully! \n you can check your status on the following credentials \nusername: " + doctorRegisterDto.getDoctorEmail() + "\npassword: " + password;

    }

    @Override
    @Cacheable(value = "AllPendingDoctors")
    public List<DoctorApplicationProjection> findAllPendingDoctors() throws DoctorsException {
        List<DoctorApplication> doctorApplicationList = doctorApplicationRepo.getAllPendingDoctors();
        if(doctorApplicationList.isEmpty()){
            throw new DoctorsException("No Pending Doctor Available",HttpStatus.NOT_FOUND);
        }
        return doctorApplicationList.stream().map(d->new DoctorApplicationProjection(
                d.getTmpDoctorName(),
                d.getTmpDoctorAge(),
                d.getTmpDoctorGender(),
                d.getTmpDoctorYearsOfExperience(),
                d.getTmpDoctorGraduateCollege(),
                d.getTmpDoctorFieldOfExpertise(),
                d.getTmpDoctorEmail(),
                d.getTmpDoctorMobile(),
                d.getTmpDoctorDetailAddress(),
                d.getTmpDoctorType(),
                d.getHospitalAppliedFor(),
                d.getTmpDoctorCountryName(),
                d.getTmpDoctorStateName(),
                d.getMedicalFacilities()
                        .stream()
                        .map(MedicalFacilities::getFacilityName)
                        .toList()
        )).toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "AllPendingDoctors",allEntries = true)
    public String removeDoctorByEmail(String email) throws DoctorApplicationException{
        Optional<DoctorApplication> doctorApplication = doctorApplicationRepo.getDoctorNameByEmail(email);
        if(doctorApplication.isEmpty()){
            throw new DoctorApplicationException("No doctor with email "+email+ " exist!",HttpStatus.NOT_FOUND);
        }
        doctorApplicationRepo.deleteDoctorApplicationByEmail(email);
        return "Doctor with +"+email+" removed Successfully";
    }
}
