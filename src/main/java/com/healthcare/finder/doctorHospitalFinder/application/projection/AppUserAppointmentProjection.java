package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppUserAppointmentProjection {
    private String doctorName;
    private String hospitalName;
    private String status;
    private LocalDateTime appointmentAppliedDate;
}
