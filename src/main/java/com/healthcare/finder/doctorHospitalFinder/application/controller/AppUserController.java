package com.healthcare.finder.doctorHospitalFinder.application.controller;


import com.healthcare.finder.doctorHospitalFinder.application.dto.AppUserRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.services.AppUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appUser")
public class AppUserController {

    @Autowired
    private AppUserServices appUserServices;
    @PostMapping("/register")
    public ResponseEntity<String> clientSignUp(@RequestBody AppUserRegisterDto appUserRegisterDto){
        System.out.println("I am from controller: "+appUserRegisterDto.getRole());
        return ResponseEntity.status(200).body(appUserServices.addAppUser(appUserRegisterDto));
    }
}
