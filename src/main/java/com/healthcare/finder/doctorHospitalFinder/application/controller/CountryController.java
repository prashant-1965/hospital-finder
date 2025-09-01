package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.CountryRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CountryListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.services.CountryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {
    @Autowired
    private CountryServices countryServices;

    @PostMapping("/register")
    public ResponseEntity<String> registerDoctor(@RequestBody CountryRegisterDto countryRegisterDto){
        return ResponseEntity.status(200).body(countryServices.addCountry(countryRegisterDto));
    }
    @GetMapping("/getCountryList")
    public ResponseEntity<List<CountryListProjection>> getCountryList(){
        return ResponseEntity.status(200).body(countryServices.getCountryList());
    }
}
