package com.healthcare.finder.doctorHospitalFinder.application.controller;


import com.healthcare.finder.doctorHospitalFinder.application.services.StateService;
import com.healthcare.finder.doctorHospitalFinder.application.projection.StateListProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/myCustom")
public class CustomisationFormController {

    @Autowired
    private StateService stateService;

    @GetMapping("/getStateList")
    public ResponseEntity<List<StateListProjection>> getStates(@RequestParam String countryName) {
        return ResponseEntity.ok(stateService.getStateList(countryName));
    }

    @GetMapping("/hospitalType")
    public ResponseEntity<List<String>> getHospitalType(){
        return ResponseEntity.status(200).body(List.of("Private","Government"));
    }
}
