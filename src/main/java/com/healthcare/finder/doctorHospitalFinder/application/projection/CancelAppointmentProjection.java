package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CancelAppointmentProjection {
    private String userName;
    private String userEmail;
    private String userMobile;
    private String appointmentStatus; // Cancel
    private String userCancelAppointmentDate;
    private String reason;
}
