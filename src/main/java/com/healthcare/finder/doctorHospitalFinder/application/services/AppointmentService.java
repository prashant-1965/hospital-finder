package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.AppointmentRegistrationDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CancelAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CompletedAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.PendingAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.UpComingAppointmentProjection;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentService {
    String registerAppointment(AppUser appUser, AppointmentRegistrationDto appointmentRegistrationDto);

    List<PendingAppointmentProjection> getPendingAppointmentsByDoctorName(String doctorName);
    List<UpComingAppointmentProjection> getUpComingAppointmentsByDoctorName(String doctorName);
    List<CompletedAppointmentProjection> getCompletedAppointmentsByDoctorName(String doctorName);
    List<CancelAppointmentProjection> getCancelAppointmentsByDoctorName(String doctorName);
}
