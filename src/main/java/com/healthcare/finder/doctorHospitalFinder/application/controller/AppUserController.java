package com.healthcare.finder.doctorHospitalFinder.application.controller;


import com.healthcare.finder.doctorHospitalFinder.application.dto.AppUserRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.services.AppUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appUser")
public class AppUserController {

    @Autowired
    private AppUserServices appUserServices;
    @PostMapping("/register")
    public ResponseEntity<String> clientSignUp(@RequestBody AppUserRegisterDto appUserRegisterDto){
        return ResponseEntity.status(200).body(appUserServices.addAppUser(appUserRegisterDto));
    }
    @PutMapping("/changePasswordRequest")
    public ResponseEntity<String> changeUserPassword(@RequestParam String userEmail, @RequestParam String password){
        return ResponseEntity.status(200).body(appUserServices.changeAppUserPasswordRequest(userEmail,password));
    }
}
