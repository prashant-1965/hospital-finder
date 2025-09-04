package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.MedicalFacilitiesRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.services.FacilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facility")
public class FacilitiesController {

    @Autowired
    private FacilitiesService facilitiesService;

    @GetMapping("/allFacilities")
    public ResponseEntity<List<FacilityListProjection>> getAvailableFacilities() {
        return ResponseEntity.status(200).body(facilitiesService.getAllAvailableFacilities());
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerFacility(@RequestBody MedicalFacilitiesRegisterDto medicalFacilitiesRegisterDto){
        return ResponseEntity.status(200).body(facilitiesService.addFacility(medicalFacilitiesRegisterDto));
    }
    @GetMapping("/facilityByDoctorName")
    public ResponseEntity<List<String>> findFacilityByDoctorEmail(@RequestParam String doctorEmail){
        return ResponseEntity.status(200).body(facilitiesService.findFacilityByDoctorEmail(doctorEmail));
    }
    @GetMapping("/allFacilityByHospitalName")
    public ResponseEntity<List<String>> findFacilityByHospitalName(@RequestParam String hospitalName){
        return ResponseEntity.status(200).body(facilitiesService.findFacilityByHospitalName(hospitalName));
    }
}
