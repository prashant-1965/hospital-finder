package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PendingAppointmentProjection {
    private String userName;
    private String userEmail;
    private String userMobile;
    private String bookedFacilityName;
    private String appointmentStatus; // pending
    private LocalDateTime userExpectedAppointmentDate;
    private LocalDateTime appliedDate;
}
