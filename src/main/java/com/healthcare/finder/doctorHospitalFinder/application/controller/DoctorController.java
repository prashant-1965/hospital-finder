package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.DoctorReviewDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.DoctorApplication;
import com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualDoctorDetailProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.DoctorApplicationRepo;
import com.healthcare.finder.doctorHospitalFinder.application.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DoctorApplicationRepo doctorApplicationRepo;

    @PostMapping("/register")
    public ResponseEntity<String> registerDoctor(@RequestParam String email){

        DoctorApplication doctorApplication = doctorApplicationRepo.getDoctorNameByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.status(200).body(doctorService.addDoctor(doctorApplication));
    }

    @GetMapping("/getTop5Doctors")
    public ResponseEntity<List<TopNDoctorListProjection>> getTop5Doctor(@RequestParam String countryName){
        return ResponseEntity.status(200).body(doctorService.getTop5DoctorListByCountry(countryName));
    }

    @GetMapping("/getTopNDoctors")
    public ResponseEntity<List<TopNDoctorListProjection>> getTopNDoctor(@RequestParam String countryName, @RequestParam int n){
        return ResponseEntity.status(200).body(doctorService.getTopNDoctorListByCountry(countryName,n));
    }

    @GetMapping("/getByExperience")
    public ResponseEntity<List<TopNDoctorListProjection>> getTopNExperiencedDoctor(@RequestParam String countryName, @RequestParam String specialisation, @RequestParam String type, @RequestParam String order){
        return ResponseEntity.status(200).body(doctorService.getNDoctorByExperienceAndSpecialisationList(countryName,specialisation,type,order));
    }

    @GetMapping("/getDoctorDetails")
    public ResponseEntity<IndividualDoctorDetailProjection> findDoctorByName(@RequestParam String doctorName){
        return ResponseEntity.status(200).body(doctorService.findDoctorDetailByName(doctorName));
    }

    @PostMapping("/addReview")
    public ResponseEntity<String> addDoctorReview(@RequestBody DoctorReviewDto doctorReviewDto){
        return ResponseEntity.status(200).body(doctorService.addDoctorReviews(doctorReviewDto));
    }

    @GetMapping("/allDoctorList")
    public ResponseEntity<List<String>> getAllDoctorList(){
        return ResponseEntity.status(200).body(doctorService.findAllAvailableDoctor());
    }
}
