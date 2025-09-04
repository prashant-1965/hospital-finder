package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.dto.AppointmentRegistrationDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.AppUserAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppUserRepo;
import com.healthcare.finder.doctorHospitalFinder.application.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/registerAppointment")
    public ResponseEntity<String> tryDoctorAppointmentBooking(@RequestBody AppointmentRegistrationDto appointmentRegistrationDto){
        return ResponseEntity.status(200).body(appointmentService.registerAppointment(appointmentRegistrationDto));
    }
    @GetMapping("/MyAppointment")
    public ResponseEntity<List<AppUserAppointmentProjection>> getAllAppointments(@RequestParam String email){
        return ResponseEntity.status(200).body(appointmentService.findAllBookedAppointmentByUserName(email));
    }
    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateAppointmentStatus(@RequestBody String email, @RequestBody String newStatus){
        return ResponseEntity.status(200).body(appointmentService.updateAppointmentStatus(email,newStatus));
    }
    @DeleteMapping("/removeAppointment")
    public ResponseEntity<String> removeAppointments(@RequestBody String email){
        return ResponseEntity.status(200).body(appointmentService.removeAppointmentsByUserEmail(email));
    }

}
