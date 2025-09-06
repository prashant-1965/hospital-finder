package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.AppointmentRegistrationDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.*;

import java.util.List;

public interface AppointmentService {
    String registerAppointment(AppointmentRegistrationDto appointmentRegistrationDto);
    List<PendingAppointmentProjection> getPendingAppointmentsByDoctorEmail(String doctorEmail);
    List<UpComingAppointmentProjection> getUpComingAppointmentsByDoctorEmail(String doctorEmail);
    List<CompletedAppointmentProjection> getCompletedAppointmentsByDoctorEmail(String doctorEmail);
    List<CancelAppointmentProjection> getCancelAppointmentsByDoctorEmail(String doctorEmail);
    List<AppUserAppointmentProjection> findAllBookedAppointmentByUserName(String userEmail);
    String removeAppointmentsByUserEmail(String userEmail);
    String updateAppointmentStatus(String userEmail, String newStatus, String facility);
}
