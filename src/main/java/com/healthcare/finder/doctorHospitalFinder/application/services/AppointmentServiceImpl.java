package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.DoctorsException;
import com.healthcare.finder.doctorHospitalFinder.application.classException.FacilitiesException;
import com.healthcare.finder.doctorHospitalFinder.application.classException.HospitalException;
import com.healthcare.finder.doctorHospitalFinder.application.dto.AppointmentRegistrationDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.*;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CancelAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CompletedAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.PendingAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.UpComingAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public String registerAppointment(AppUser appUser, AppointmentRegistrationDto appointmentRegistrationDto) throws DoctorsException, HospitalException, FacilitiesException {

        Doctor doctor;
        if(appointmentRegistrationDto.getDoctorName()!=null) {
            doctor = doctorRepo.findByDoctorName(appointmentRegistrationDto.getDoctorName());
            if (doctor == null) {
                throw new DoctorsException("Invalid Doctor Name", HttpStatus.BAD_REQUEST);
            }
        }else {
            doctor = null;
        }
        Hospital hospital;
        if(appointmentRegistrationDto.getHospitalName()!=null) {
            hospital = hospitalRepo.findByHospitalName(appointmentRegistrationDto.getHospitalName());
            if (hospital == null) {
                throw new HospitalException("Invalid Hospital Name", HttpStatus.BAD_REQUEST);
            }
        }else{
            hospital = null;
        }
        MedicalFacilities facility = facilitiesRepo.findByFacilityName(appointmentRegistrationDto.getFacilityName());
        if(facility==null){
            throw new FacilitiesException("Invalid Facility Name", HttpStatus.BAD_REQUEST);
        }
        Appointment appointment = new Appointment();
        appointment.setUser(appUser);
        appointment.setFacilities(facility);
        appointment.setDoctor(doctor);
        appointment.setHospital(hospital);
        appointment.setAppointmentStatus("pending");
        appointment.setAppointmentDate(appointmentRegistrationDto.getLocalDateTime());
        appointmentRepo.save(appointment);
        return "Request Sent SuccessFully";
    }

    @Override
    public List<PendingAppointmentProjection> getPendingAppointmentsByDoctorName(String doctorName) {
        return List.of();
    }

    @Override
    public List<UpComingAppointmentProjection> getUpComingAppointmentsByDoctorName(String doctorName) {
        return List.of();
    }

    @Override
    public List<CompletedAppointmentProjection> getCompletedAppointmentsByDoctorName(String doctorName) {
        return List.of();
    }

    @Override
    public List<CancelAppointmentProjection> getCancelAppointmentsByDoctorName(String doctorName) {
        return List.of();
    }
}
