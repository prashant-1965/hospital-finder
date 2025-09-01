package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.projection.*;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppUserRepo;
import com.healthcare.finder.doctorHospitalFinder.application.services.AppointmentService;
import com.healthcare.finder.doctorHospitalFinder.application.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboardDoctor")
public class DoctorHomeDashboardController {
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private HospitalService hospitalService;


    @GetMapping()
    public ResponseEntity<Map<String,Object>> getFrontPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User springUser = (User) authentication.getPrincipal();
        String email = springUser.getUsername();
        AppUser appUser = appUserRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<PendingAppointmentProjection> pendingAppointmentProjections = appointmentService.getPendingAppointmentsByDoctorName(appUser.getUserName());
        List<UpComingAppointmentProjection> upComingAppointmentProjections = appointmentService.getUpComingAppointmentsByDoctorName(appUser.getUserName());
        List<CompletedAppointmentProjection> completedAppointmentProjections = appointmentService.getCompletedAppointmentsByDoctorName(appUser.getUserName());
        List<CancelAppointmentProjection> canceledAppointmentsProjections = appointmentService.getCancelAppointmentsByDoctorName(appUser.getUserName());
        List<TopNHospitalListProjection> topNHospitalListProjections = hospitalService.getTopNHospitalList();

        Map<String, Object> details = new HashMap<>();
        details.put("CurrentUserName",appUser.getUserName());
        details.put("PendingAppointments",pendingAppointmentProjections);
        details.put("UpComingAppointments",upComingAppointmentProjections);
        details.put("completedAppointments",completedAppointmentProjections);
        details.put("canceledAppointments",canceledAppointmentsProjections);
        details.put("hospitalListForJobApply",topNHospitalListProjections);

        return ResponseEntity.status(200).body(details);
    }
}
