package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.AppointmentRegistrationDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppUserRepo;
import com.healthcare.finder.doctorHospitalFinder.application.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/register")
    public ResponseEntity<String> tryAppointmentBooking(@RequestBody AppointmentRegistrationDto appointmentRegistrationDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String email = user.getUsername();
        AppUser appUser = appUserRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.status(200).body(appointmentService.registerAppointment(appUser,appointmentRegistrationDto));
    }

}
