package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendingAppointmentProjection {
    private String userName;
    private String userEmail;
    private String userAge;
    private String userMobile;
    private String appointmentStatus; // pending
    private String userExpectedAppointmentDate;
    private String appliedDate;
}
