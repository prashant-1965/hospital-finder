package com.healthcare.finder.doctorHospitalFinder.application.controller;


import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;
import com.healthcare.finder.doctorHospitalFinder.application.services.DoctorService;
import com.healthcare.finder.doctorHospitalFinder.application.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/govHospital")
public class GovDashboardController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private HospitalService hospitalService;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getFrontPage(@RequestParam Map<String,String> customData){

        Map<String, Object> map = new HashMap<>();

        map.put("Current Location: ",customData.get("country")+" "+customData.get("state"));
        List<TopNDoctorListProjection> topNDoctorListProjections = doctorService.getTopNGovDoctorBySpecialisationList(customData.get("countryName"), customData.get("specialisation"));
        map.put("topNGovSpecialistDoctor",topNDoctorListProjections);

        List<TopNHospitalListProjection> topNHospitalListProjections = hospitalService.getTopNGovHospitalList(customData.get("countryName"), customData.get("specialisation"));
        map.put("topNGovSpecialistHospitals",topNHospitalListProjections);

        return ResponseEntity.status(200).body(map);
    }
}
