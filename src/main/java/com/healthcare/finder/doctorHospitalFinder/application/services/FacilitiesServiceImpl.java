package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.FacilitiesException;
import com.healthcare.finder.doctorHospitalFinder.application.dto.MedicalFacilitiesRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.MedicalFacilities;
import com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.FacilitiesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilitiesServiceImpl implements FacilitiesService {

    @Autowired
    private FacilitiesRepo facilitiesRepo;

    @Override
    @Cacheable(value = "FacilityListProjection")
    public List<FacilityListProjection> getAllAvailableFacilities() throws FacilitiesException {
        List<FacilityListProjection> getfacilitiesList = facilitiesRepo.getAllAvailableFacility();
        if (getfacilitiesList.isEmpty()){
            throw new FacilitiesException("No facility Available", HttpStatus.NOT_FOUND);
        }
        return getfacilitiesList.stream().sorted().toList();
    }

    @Override
    @Caching(evict={
                @CacheEvict(value = "FacilityListProjection",allEntries = true),
                @CacheEvict(value = "facilityListByHospitalName",allEntries = true),
                @CacheEvict(value = "MedicalFacilities",allEntries = true)
            }
    )
    public String addFacility(MedicalFacilitiesRegisterDto medicalFacilitiesRegisterDto) throws FacilitiesException {
        if(medicalFacilitiesRegisterDto.getFacilityName().isEmpty()){
            throw new FacilitiesException("Invalid facility name",HttpStatus.BAD_REQUEST);
        }
        if(medicalFacilitiesRegisterDto.getFacilityDescription().isEmpty()){
            throw new FacilitiesException("Provide facility description",HttpStatus.BAD_REQUEST);
        }
        MedicalFacilities medicalFacilitiesExist = this.findByFacilityName(medicalFacilitiesRegisterDto.getFacilityName());
        if(medicalFacilitiesExist!=null){
            throw new FacilitiesException("Facility Already Exist",HttpStatus.BAD_REQUEST);
        }
        MedicalFacilities medicalFacilities = new MedicalFacilities();
        medicalFacilities.setFacilityName(medicalFacilitiesRegisterDto.getFacilityName());
        medicalFacilities.setFacilityDescription(medicalFacilitiesRegisterDto.getFacilityDescription());
        facilitiesRepo.save(medicalFacilities);
        return "Facility Added Successfully";
    }

    @Override
    @Cacheable(value = "doctorListByDoctorEmail",key = "#doctorEmail",condition = "#doctorEmail!=null")
    public List<String> findFacilityByDoctorEmail(String doctorEmail) throws FacilitiesException {
        List<String> doctorList = facilitiesRepo.findFacilityByDoctorEmail(doctorEmail);
        if(doctorList.isEmpty()){
            throw new FacilitiesException(doctorEmail +" is not providing any Facility!",HttpStatus.NOT_FOUND);
        }
        return doctorList;
    }

    @Override
    @Cacheable(value = "facilityListByHospitalName",key = "#hospitalName", unless = "#result==null")
    public List<String> findFacilityByHospitalName(String hospitalName) throws FacilitiesException {
        List<String> facilityList = facilitiesRepo.getFacilityByHospitalName(hospitalName);
        if (facilityList.isEmpty()){
            throw new FacilitiesException(hospitalName+" doesn't provide any facility",HttpStatus.NOT_FOUND);
        }
        return facilityList;
    }

    @Override
    @Cacheable(value = "MedicalFacilities",key = "#facilityName", unless = "#result==null")
    public MedicalFacilities findByFacilityName(String facilityName) {
        return facilitiesRepo.findByFacilityName(facilityName);
    }
}
