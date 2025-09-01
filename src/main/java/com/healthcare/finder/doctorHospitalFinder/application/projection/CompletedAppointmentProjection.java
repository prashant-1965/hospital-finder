package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompletedAppointmentProjection {
    private String userName;
    private String userEmail;
    private String userMobile;
    private String appointmentStatus; // Completed
    private String userVisitedAppointmentDate;
    private String ratting;
    private String comments;
}
