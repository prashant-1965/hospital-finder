package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.DoctorRegisterDto;

import com.healthcare.finder.doctorHospitalFinder.application.dto.HospitalRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.services.DoctorApplicationService;
import com.healthcare.finder.doctorHospitalFinder.application.services.HospitalApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contactUs")
public class ContactUsController {
    @Autowired
    private DoctorApplicationService doctorApplicationService;
    @Autowired
    private HospitalApplicationService hospitalApplicationService;

    @PostMapping("/doctorRegistrationRequest")
    public ResponseEntity<String> doctorRegistrationRequest(@RequestBody DoctorRegisterDto doctorRegisterDto){
        return ResponseEntity.status(200).body(doctorApplicationService.addDoctorApplicationRequest(doctorRegisterDto));
    }
    @PostMapping("/doctorRemovalRequest")
    public ResponseEntity<String> doctorRemovalRequest(@RequestBody String doctorEmail){
        return ResponseEntity.status(200).body(doctorApplicationService.removeDoctorByEmail(doctorEmail));
    }
    @PostMapping("/hospitalRegistrationRequest")
    public ResponseEntity<String> hospitalRegistrationRequest(@RequestBody HospitalRegisterDto hospitalRegisterDto){
        return ResponseEntity.status(200).body(hospitalApplicationService.addHospitalRegistrationRequest(hospitalRegisterDto));
    }
    @PostMapping("/hospitalRemovalRequest")
    public ResponseEntity<String> hospitalRemovalRequest(@RequestBody String HospitalName){
        return ResponseEntity.status(200).body(hospitalApplicationService.hospitalRemovalRequest(HospitalName));
    }
}
