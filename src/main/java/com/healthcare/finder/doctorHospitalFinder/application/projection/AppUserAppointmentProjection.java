package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppUserAppointmentProjection  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String doctorName;
    private String hospitalName;
    private String status;
    private LocalDateTime appointmentAppliedDate;
}
