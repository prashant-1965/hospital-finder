package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.projection.Top10RattingCommentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.services.AppUserServices;
import com.healthcare.finder.doctorHospitalFinder.application.services.DoctorService;
import com.healthcare.finder.doctorHospitalFinder.application.services.HospitalService;
import com.healthcare.finder.doctorHospitalFinder.application.services.GlobalReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboardPublic")
public class HomeDashboardController {
    @Autowired
    private AppUserServices clientServices;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private GlobalReviewService globalReviewService;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getFrontPage(){

        String defaultCountryName = "India";
        String defaultStateName = "New Delhi";
        List<TopNDoctorListProjection> top10DoctorListProjections = doctorService.getTop10DoctorList();

        List<TopNHospitalListProjection> top10HospitalListProjections = hospitalService.getTopNHospitalList();

        List<Top10RattingCommentProjection> top10RattingCommentProjections = globalReviewService.getTop10RecentGlobalReviews();

        Map<String, Object> response = new HashMap<>();
        response.put("locationDetails", defaultStateName+", "+defaultCountryName);
        response.put("top5Doctors", top10DoctorListProjections);
        response.put("top5Hospitals",top10HospitalListProjections);
        response.put("top10RattingComment",top10RattingCommentProjections);

        return ResponseEntity.status(200).body(response);
    }
    @GetMapping("/reviewList")
    public ResponseEntity<List<String>> getAvailableReviewOptions(){
        return ResponseEntity.status(200).body(List.of("Rate our Doctors","Rate our Hospitals","Rate our OverallService"));
    }
}
