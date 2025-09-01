package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.FacilitiesException;
import com.healthcare.finder.doctorHospitalFinder.application.dto.MedicalFacilitiesRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.MedicalFacilities;
import com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.FacilitiesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilitiesServiceImpl implements FacilitiesService {

    @Autowired
    private FacilitiesRepo facilitiesRepo;

    @Override
    public List<FacilityListProjection> getAllAvailableFacilities() throws FacilitiesException {
        List<FacilityListProjection> getfacilitiesList = facilitiesRepo.getAllAvailableFacility();
        if (getfacilitiesList.isEmpty()){
            throw new FacilitiesException("No facility Available", HttpStatus.NOT_FOUND);
        }
        return getfacilitiesList.stream().sorted().toList();
    }

    @Override
    public String addFacility(MedicalFacilitiesRegisterDto medicalFacilitiesRegisterDto) throws FacilitiesException {
        if(medicalFacilitiesRegisterDto.getFacilityName().isEmpty()){
            throw new FacilitiesException("Invalid facility name",HttpStatus.BAD_REQUEST);
        }
        if(medicalFacilitiesRegisterDto.getFacilityDescription().isEmpty()){
            throw new FacilitiesException("Provide facility description",HttpStatus.BAD_REQUEST);
        }
        MedicalFacilities medicalFacilitiesExist = facilitiesRepo.findByFacilityName(medicalFacilitiesRegisterDto.getFacilityName());
        if(medicalFacilitiesExist!=null){
            throw new FacilitiesException("Facility Already Exist",HttpStatus.BAD_REQUEST);
        }
        MedicalFacilities medicalFacilities = new MedicalFacilities();
        medicalFacilities.setFacilityName(medicalFacilitiesRegisterDto.getFacilityName());
        medicalFacilities.setFacilityDescription(medicalFacilitiesRegisterDto.getFacilityDescription());
        facilitiesRepo.save(medicalFacilities);
        return "Facility Added Successfully";
    }
}
