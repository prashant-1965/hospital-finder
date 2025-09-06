package com.healthcare.finder.doctorHospitalFinder.application.controller;

import com.healthcare.finder.doctorHospitalFinder.application.classException.AppointmentException;
import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.projection.*;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppUserRepo;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppointmentRepo;
import com.healthcare.finder.doctorHospitalFinder.application.services.AppointmentService;
import com.healthcare.finder.doctorHospitalFinder.application.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
    @Autowired
    private AppointmentRepo appointmentRepo;


    @GetMapping()
    public ResponseEntity<Map<String,Object>> getFrontPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User springUser = (User) authentication.getPrincipal();
        String email = springUser.getUsername();
        AppUser appUser = appUserRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<PendingAppointmentProjection> pendingAppointmentProjections = appointmentService.getPendingAppointmentsByDoctorEmail(appUser.getUserEmail());
        List<UpComingAppointmentProjection> upComingAppointmentProjections = appointmentService.getUpComingAppointmentsByDoctorEmail(appUser.getUserEmail());
        List<CompletedAppointmentProjection> completedAppointmentProjections = appointmentService.getCompletedAppointmentsByDoctorEmail(appUser.getUserEmail());
        List<CancelAppointmentProjection> canceledAppointmentsProjections = appointmentService.getCancelAppointmentsByDoctorEmail(appUser.getUserEmail());
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
//    @GetMapping("/getPendingAppointment")
//    public ResponseEntity<List<PendingAppointmentProjection>> getAllPendingAppointment(@RequestParam String email) throws AppointmentException {
//        List<PendingAppointmentProjection> appointmentPendinglList = appointmentRepo.findPendingAppointmentsByDoctorEmail(email);
//        if (appointmentPendinglList.isEmpty()) throw new AppointmentException("You have not booked any appointment till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
//        return ResponseEntity.status(200).body(appointmentPendinglList);
//    }
//    @GetMapping("/getUpComingAppointment")
//    public ResponseEntity<List<UpComingAppointmentProjection>> getAllUpComingAppointments(@RequestParam String doctorEmail) throws AppointmentException {
//        List<UpComingAppointmentProjection> appointmentUpCominglList = appointmentRepo.findUpComingAppointmentsByDoctorEmail(doctorEmail);
//        if (appointmentUpCominglList.isEmpty()) throw new AppointmentException("There are not any upcoming appointment till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
//        return ResponseEntity.status(200).body(appointmentUpCominglList);
//    }
//    @GetMapping("/getCompletedAppointment")
//    public ResponseEntity<List<CompletedAppointmentProjection>> getAllUpCompletedAppointments(@RequestParam String doctorEmail) throws AppointmentException {
//        List<CompletedAppointmentProjection> appointmentCompletelList = appointmentRepo.findCompletedAppointmentsByDoctorEmail(doctorEmail);
//        if (appointmentCompletelList.isEmpty()) throw new AppointmentException("No appointment record available till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
//        return ResponseEntity.status(200).body(appointmentCompletelList);
//    }
//    @GetMapping("/getCancelAppointment")
//    public ResponseEntity<List<CancelAppointmentProjection>> getAllUpCancelAppointments(@RequestParam String doctorEmail) throws AppointmentException {
//        List<CancelAppointmentProjection> appointmentCancelList = appointmentRepo.findCancelAppointmentsByDoctorEmail(doctorEmail);
//        if (appointmentCancelList.isEmpty()) throw new AppointmentException("No appointment record available till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
//        return ResponseEntity.status(200).body(appointmentCancelList);
//    }
}
