package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CancelAppointmentProjection {
    private String userName;
    private String userEmail;
    private String facilityBookedName;
    private String appointmentStatus; // Cancel
    private LocalDateTime userCancelAppointmentDate;
    private LocalDateTime appointmentAppliedDate;
}
