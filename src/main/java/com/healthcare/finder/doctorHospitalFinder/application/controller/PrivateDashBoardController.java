package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppUserRepo;
import com.healthcare.finder.doctorHospitalFinder.application.services.DoctorService;
import com.healthcare.finder.doctorHospitalFinder.application.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/privateHospital")
public class PrivateDashBoardController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private AppUserRepo appUserRepo;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getFrontPage(@RequestParam Map<String,String> customData){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User springUser = (User) authentication.getPrincipal();
        String email = springUser.getUsername();

        AppUser appUser = appUserRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> map = new HashMap<>();

        map.put("currentUser",appUser.getUserName());

        map.put("Current Location: ",customData.get("country")+" "+customData.get("state"));

        List<TopNDoctorListProjection> topNDoctorListProjections = doctorService.getTopNPrivateDoctorBySpecialisationList(customData.get("countryName"), customData.get("specialisation"));
        map.put("topNPrivateSpecialistDoctor",topNDoctorListProjections);

        List<TopNHospitalListProjection> topNHospitalListProjections = hospitalService.getTopNPrivateHospitalList(customData.get("countryName"), customData.get("specialisation"));
        map.put("topNPrivateSpecialistHospitals",topNHospitalListProjections);

        return ResponseEntity.status(200).body(map);
    }
}
