package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.projection.Top10RattingCommentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppUserRepo;
import com.healthcare.finder.doctorHospitalFinder.application.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard/patient")
public class PatientHomeDashBoardController {
    @Autowired
    private AppUserServices clientServices;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private GlobalReviewService reviewService;
    @Autowired
    private AppUserRepo appUserRepo;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getFrontPage(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User springUser = (User) authentication.getPrincipal();
        String email = springUser.getUsername();
        AppUser appUser = appUserRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<TopNDoctorListProjection> topDoctorListProjections = doctorService.getTopNDoctorListByCountry(appUser.getUserCountry(), 10);

        List<TopNHospitalListProjection> topHospitalListProjections = hospitalService.getTopNHospitalListByCountry(appUser.getUserCountry(),10);

        List<Top10RattingCommentProjection> top10RattingCommentProjections = reviewService.getTop10RecentGlobalReviews();

        Map<String, Object> response = new HashMap<>();
        response.put("currentUser",appUser.getUserName());
        response.put("locationDetails", appUser.getUserState()+" "+appUser.getUserCountry());
        response.put("top5Doctors", topDoctorListProjections);
        response.put("top5Hospitals",topHospitalListProjections);
        response.put("top10RattingComment",top10RattingCommentProjections);

        return ResponseEntity.ok(response);
    }
}
