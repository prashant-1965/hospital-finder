package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpComingAppointmentProjection {
    private String userName;
    private String userEmail;
    private String userMobile;
    private String facilityBooked;
    private String appointmentStatus; // upComing
    private LocalDateTime userConfirmedAppointmentDate;
    private LocalDateTime appliedDate;
}
