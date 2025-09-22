package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.*;
import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.*;
import com.healthcare.finder.doctorHospitalFinder.application.projection.HospitalApplicationProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalApplicationServiceImpl implements HospitalApplicationService{

    @Autowired
    private HospitalApplicationRepo hospitalApplicationRepo;
    @Autowired
    private CountryServices countryServices;
    @Autowired
    private StateService stateService;
    @Autowired
    private FacilitiesService facilitiesService;
    @Autowired
    private HospitalService hospitalService;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "HospitalApplication",allEntries = true),
            @CacheEvict(value = "AllPendingHospitalRequest",allEntries = true)
    })
    public String addHospitalRegistrationRequest(HospitalRegisterDto hospitalRegisterDto) throws CountryException, StateException, FacilitiesException, HospitalException {
        Country country = countryServices.findCountryByName(hospitalRegisterDto.getCountryName());
        if(country==null){
            throw new CountryException("Service is not available in "+hospitalRegisterDto.getCountryName(),HttpStatus.NOT_FOUND);
        }
        State state = stateService.findByStateName(hospitalRegisterDto.getStateName());
        if(state==null){
            throw new StateException("Our service is not available in "+hospitalRegisterDto.getStateName(),HttpStatus.NOT_FOUND);
        }
        List<MedicalFacilities> medicalFacilitiesList = new ArrayList<>();
        for(String facility:hospitalRegisterDto.getFacilities()){
            MedicalFacilities medicalFacilities = facilitiesService.findByFacilityName(facility);
            if(medicalFacilities==null){
                throw new FacilitiesException(facility+" facility is not Available",HttpStatus.BAD_REQUEST);
            }
            medicalFacilitiesList.add(medicalFacilities);
        }
        Hospital existHospital = hospitalService.findByHospitalName(hospitalRegisterDto.getHospitalName());
        if(existHospital!=null){
            throw new HospitalException(hospitalRegisterDto.getHospitalName()+" already exist!",HttpStatus.BAD_REQUEST);
        }
        HospitalApplication hospitalApplication = new HospitalApplication();
        hospitalApplication.setTempHospitalName(hospitalRegisterDto.getHospitalName());
        hospitalApplication.setTempHospitalContact(hospitalRegisterDto.getHospitalContact());
        hospitalApplication.setTempHospitalAddress(hospitalRegisterDto.getHospitalAddress());
        hospitalApplication.setTempHospitalType(hospitalRegisterDto.getHospitalType());
        hospitalApplication.setTempHospitalNumOfUsersServed(hospitalRegisterDto.getHospitalNumOfUsersServed());
        hospitalApplication.setTempHospitalYearOfEstablishment(hospitalRegisterDto.getHospitalYearOfEstablishment());
        hospitalApplication.setTempCountryName(hospitalRegisterDto.getCountryName());
        hospitalApplication.setTempStateName(hospitalRegisterDto.getStateName());
        hospitalApplication.setFacilities(medicalFacilitiesList);
        hospitalApplicationRepo.save(hospitalApplication);
        return "Request for "+hospitalRegisterDto.getHospitalName()+" registration has been sent and we will notify you shortly";
    }

    @Override
    @Cacheable(value = "AllPendingHospitalRequest")
    public List<HospitalApplicationProjection> findAllPendingHospitalRequest() throws HospitalApplicationException {
        List<HospitalApplication> hospitalApplicationList = hospitalApplicationRepo.getAllPendingHospitalApplicationList();
        if(hospitalApplicationList.isEmpty()){
            throw new HospitalApplicationException("No Pending Hospital Registration Request", HttpStatus.NOT_FOUND);
        }
        return hospitalApplicationList.stream()
                .map(h -> new HospitalApplicationProjection(
                        h.getTempHospitalName(),
                        h.getTempHospitalType(),
                        h.getTempHospitalYearOfEstablishment(),
                        h.getTempHospitalNumOfUsersServed(),
                        h.getTempHospitalRating(),
                        h.getTempHospitalContact(),
                        h.getTempHospitalAddress(),
                        h.getTempCountryName(),
                        h.getTempStateName(),
                        h.getFacilities()
                                .stream()
                                .map(MedicalFacilities::getFacilityName)
                                .toList()
                ))
                .toList();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "HospitalApplication",allEntries = true),
            @CacheEvict(value = "AllPendingHospitalRequest",allEntries = true)
    })
    public String hospitalRemovalRequest(String hospitalName) throws HospitalApplicationException {
        HospitalApplication hospitalApplication = this.getByHospitalName(hospitalName);
        if(hospitalApplication==null){
            throw new HospitalApplicationException("No Hospital with name "+hospitalName+" Found!",HttpStatus.NOT_FOUND);
        }
        hospitalApplicationRepo.deleteHospitalApplicationByName(hospitalName);
        return hospitalName+" removed SuccessFully";
    }

    @Override
    @Cacheable(value = "HospitalApplication",key = "#hospitalName",unless = "#result==null")
    public HospitalApplication getByHospitalName(String hospitalName) {
        return hospitalApplicationRepo.getByHospitalName(hospitalName);
    }
}
