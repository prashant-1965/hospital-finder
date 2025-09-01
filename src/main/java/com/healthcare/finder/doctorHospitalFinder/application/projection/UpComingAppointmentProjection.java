package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpComingAppointmentProjection {
    private String userName;
    private String userEmail;
    private String userAge;
    private String userMobile;
    private String appointmentStatus; // Upcoming
    private String userConfirmedAppointmentDate;
    private String appliedDate;
}
