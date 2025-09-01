package com.healthcare.finder.doctorHospitalFinder.application.controller;


import com.healthcare.finder.doctorHospitalFinder.application.dto.CountryRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.services.CountryServices;
import com.healthcare.finder.doctorHospitalFinder.application.services.FacilitiesService;
import com.healthcare.finder.doctorHospitalFinder.application.services.StateService;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CountryListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.StateListProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/myCustom")
public class CustomisationFormController {

    @Autowired
    private StateService stateService;
    @Autowired
    private CountryServices countryServices;
    @Autowired
    private FacilitiesService facilities;

    @GetMapping("/getCountryList")
    public ResponseEntity<List<CountryListProjection>> getCountryList(){
        return ResponseEntity.status(200).body(countryServices.getCountryList());
    }

    @GetMapping("/getStateList/{countryName}")
    public ResponseEntity<List<StateListProjection>> getStates(@PathVariable String countryName) {
        return ResponseEntity.ok(stateService.getStateList(countryName));
    }

    @GetMapping("/hospitalType")
    public ResponseEntity<List<String>> getHospitalType(){
        return ResponseEntity.status(200).body(List.of("Private","Government"));
    }

    @GetMapping("/allFacilities")
    public ResponseEntity<List<FacilityListProjection>> getAvailableFacilities(){
        return ResponseEntity.status(200).body(facilities.getAllAvailableFacilities());
    }
}
