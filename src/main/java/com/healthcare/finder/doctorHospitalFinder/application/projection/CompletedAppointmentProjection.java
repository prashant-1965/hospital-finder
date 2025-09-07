package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CompletedAppointmentProjection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String userName;
    private String userEmail;
    private String facilityBookedName;
    private String appointmentStatus; // Completed
    private LocalDateTime userVisitedAppointmentDate;
    private LocalDateTime appointmentAppliedDate;
}
