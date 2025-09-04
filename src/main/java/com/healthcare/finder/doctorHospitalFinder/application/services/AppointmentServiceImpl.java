package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.*;
import com.healthcare.finder.doctorHospitalFinder.application.dto.AppointmentRegistrationDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.*;
import com.healthcare.finder.doctorHospitalFinder.application.projection.*;
import com.healthcare.finder.doctorHospitalFinder.application.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private FacilitiesRepo facilitiesRepo;
    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    @Transactional
    public String registerAppointment(AppointmentRegistrationDto appointmentRegistrationDto) throws DoctorsException, HospitalException, FacilitiesException {

        Doctor doctor = doctorRepo.findByDoctorName(appointmentRegistrationDto.getDoctorName());
        Optional<Hospital> hospital = hospitalRepo.getHospitalByDoctorName(appointmentRegistrationDto.getDoctorName());
        MedicalFacilities facility = facilitiesRepo.findByFacilityName(appointmentRegistrationDto.getFacilityName());
        Optional<AppUser> appUser = appUserRepo.findByUserEmail(appointmentRegistrationDto.getAppUserEmail());
        Appointment appointment = new Appointment();
        appointment.setUser(appUser.get());
        appointment.setFacilities(facility);
        appointment.setDoctor(doctor);
        appointment.setHospital(hospital.get());
        appointment.setAppointmentStatus("pending");
        appointment.setAppointmentAppliedDate(LocalDateTime.now());
        appointment.setAppointmentDate(appointmentRegistrationDto.getLocalDateTime());
        appointmentRepo.save(appointment);
        return "Request Sent SuccessFully";
    }

    @Override
    public List<PendingAppointmentProjection> getPendingAppointmentsByDoctorEmail(String doctorEmail) {
        List<PendingAppointmentProjection> appointmentPendinglList = appointmentRepo.findPendingAppointmentsByDoctorEmail(doctorEmail);
        if (appointmentPendinglList.isEmpty()) throw new AppointmentException("You have not booked any appointment till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentPendinglList;
    }

    @Override
    public List<UpComingAppointmentProjection> getUpComingAppointmentsByDoctorEmail(String doctorEmail) {
        List<UpComingAppointmentProjection> appointmentUpCominglList = appointmentRepo.findUpComingAppointmentsByDoctorEmail(doctorEmail);
        if (appointmentUpCominglList.isEmpty()) throw new AppointmentException("You have not booked any appointment till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentUpCominglList;
    }

    @Override
    public List<CompletedAppointmentProjection> getCompletedAppointmentsByDoctorEmail(String doctorEmail) {
        List<CompletedAppointmentProjection> appointmentCompletelList = appointmentRepo.findCompletedAppointmentsByDoctorEmail(doctorEmail);
        if (appointmentCompletelList.isEmpty()) throw new AppointmentException("You have not booked any appointment till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentCompletelList;
    }

    @Override
    public List<CancelAppointmentProjection> getCancelAppointmentsByDoctorEmail(String doctorEmail) throws AppointmentException {
        List<CancelAppointmentProjection> appointmentCancelList = appointmentRepo.findCancelAppointmentsByDoctorEmail(doctorEmail);
        if (appointmentCancelList.isEmpty()) throw new AppointmentException("You have not booked any appointment till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentCancelList;
    }

    @Override
    public List<AppUserAppointmentProjection> findAllBookedAppointmentByUserName(String userEmail) throws AppointmentException {
        List<AppUserAppointmentProjection> appointmentList = appointmentRepo.findAllAppointmentListByUserEmail(userEmail);
        if (appointmentList.isEmpty()) throw new AppointmentException("You have not booked any appointment till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentList;
    }

    @Override
    @Transactional
    public String removeAppointmentsByUserEmail(String userEmail) {
        appointmentRepo.deleteAppointmentByAppUserEmail(userEmail);
        return "User with "+ userEmail +" has removed SuccessFully";
    }

    @Override
    public String updateAppointmentStatus(String userEmail, String newStatus) {
        appointmentRepo.updateAppointmentStatusByUserEmail(userEmail,newStatus,LocalDateTime.now());
        return "User with "+ userEmail +"'s status has been updated to "+newStatus;
    }
}
