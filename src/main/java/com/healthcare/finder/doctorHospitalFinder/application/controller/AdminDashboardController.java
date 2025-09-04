package com.healthcare.finder.doctorHospitalFinder.application.controller;


import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.projection.DoctorApplicationProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.HospitalApplicationProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppUserRepo;
import com.healthcare.finder.doctorHospitalFinder.application.services.DoctorApplicationService;
import com.healthcare.finder.doctorHospitalFinder.application.services.HospitalApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adminHome")
public class AdminDashboardController {

    @Autowired
    private DoctorApplicationService doctorApplicationService;
    @Autowired
    private HospitalApplicationService hospitalApplicationService;
    @Autowired
    private AppUserRepo appUserRepo;

    @GetMapping()
    public ResponseEntity<Map<String,Object>> adminHomePage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User springUser = (User) authentication.getPrincipal();
        String email = springUser.getUsername();
        AppUser appUser = appUserRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Map<String,Object> map=  new HashMap<>();
        String adminName = appUser.getUserName();
        List<DoctorApplicationProjection> pendingDoctorList = doctorApplicationService.findAllPendingDoctors();
        List<HospitalApplicationProjection> pendingHospitalList = hospitalApplicationService.findAllPendingHospitalRequest();

        map.put("adminName",adminName);
        map.put("pendingDoctorApplications",pendingDoctorList);
        map.put("pendingHospitalApplications",pendingHospitalList);

        return ResponseEntity.status(200).body(map);
    }
}
