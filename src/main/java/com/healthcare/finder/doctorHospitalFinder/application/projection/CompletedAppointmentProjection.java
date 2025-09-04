package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CompletedAppointmentProjection {
    private String userName;
    private String userEmail;
    private String appointmentStatus; // Completed
    private LocalDateTime userVisitedAppointmentDate;
    private LocalDateTime appointmentAppliedDate;
}
