package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.StateRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.services.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/state")
public class StateController {
    @Autowired
    private StateService stateService;

    @PostMapping("/register")
    public ResponseEntity<String> registerDoctor(@RequestBody StateRegisterDto stateRegisterDto){
        return ResponseEntity.status(200).body(stateService.addState(stateRegisterDto));
    }
}
