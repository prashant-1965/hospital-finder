package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.entity.Doctor;
import com.healthcare.finder.doctorHospitalFinder.application.entity.DoctorApplication;
import com.healthcare.finder.doctorHospitalFinder.application.services.DoctorApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DoctorApplicationService doctorApplicationService;

    @GetMapping()
    public ResponseEntity<Map<String,Object>> adminHomePage(){
        Map<String,Object> map=  new HashMap<>();
        List<DoctorApplication> pendingDoctorList = doctorApplicationService.findAllPendingDoctors();
        map.put("pendingDoctorApplications",pendingDoctorList);
        return ResponseEntity.status(200).body(map);
    }
}
