package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalReviewDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualHospitalDetailProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/register")
    public ResponseEntity<String> addHospital(@RequestParam String hospitalName){
        return ResponseEntity.status(200).body(hospitalService.registerHospital(hospitalName));
    }

    @GetMapping("/getTop5hospitals")
    public ResponseEntity<List<TopNHospitalListProjection>> getTop5Hospital(@RequestParam String countryName){
        return ResponseEntity.status(200).body(hospitalService.getTopNHospitalListByCountry(countryName,5));
    }

    @GetMapping("/getTopNHospitals")
    public ResponseEntity<List<TopNHospitalListProjection>> getTopNHospital(@RequestParam String countryName, @RequestParam int n){
        return ResponseEntity.status(200).body(hospitalService.getTopNHospitalListByCountry(countryName,n));
    }
    @PostMapping("/addReview")
    public ResponseEntity<String> addDoctorReview(@RequestBody HospitalReviewDto hospitalReviewDto){
        return ResponseEntity.status(200).body(hospitalService.addHospitalReviews(hospitalReviewDto));
    }
    @GetMapping("/allHospitalList")
    public ResponseEntity<List<String>> getAllHospitalList(){
        return ResponseEntity.status(200).body(hospitalService.findAllAvailableHospital());
    }
    @GetMapping("/findIndividualHospitalDetail")
    public ResponseEntity<IndividualHospitalDetailProjection> findIndividualHospitalDetailByHospitalName(String hospitalName){
        return ResponseEntity.status(200).body(hospitalService.findHospitalDetailByName(hospitalName));
    }
    @GetMapping("/findHospitalByDoctorEmail")
    public ResponseEntity<String> findHospitalByDoctorName(@RequestParam String doctorEmail){
        return ResponseEntity.status(200).body(hospitalService.findHospitalByDoctoEmail(doctorEmail));
    }
    @GetMapping("/allHospitalByFacilityName")
    public ResponseEntity<List<String>> findHospitalByFacilityName(@RequestParam String facilityName){
        return ResponseEntity.status(200).body(hospitalService.findHospitalByFacilityName(facilityName));
    }
}
