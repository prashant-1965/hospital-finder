package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.*;
import com.healthcare.finder.doctorHospitalFinder.application.dto.AppointmentRegistrationDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.*;
import com.healthcare.finder.doctorHospitalFinder.application.projection.*;
import com.healthcare.finder.doctorHospitalFinder.application.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private FacilitiesService facilitiesService;
    @Autowired
    private AppUserServices appUserServices;

    @Override
    @Transactional
    @CacheEvict(value = "AllPendingAppointmentsByDoctorEmail",allEntries = true)
    public String registerAppointment(AppointmentRegistrationDto appointmentRegistrationDto) throws DoctorsException, HospitalException, FacilitiesException {

        Optional<Appointment> optionalAppointment = appointmentRepo.appointmentExistByDoctorEmailAndFacility(appointmentRegistrationDto.getDoctorName(),appointmentRegistrationDto.getFacilityName());
        if (optionalAppointment.isPresent()){
            throw new AppointmentException("You have already book appointment with "+appointmentRegistrationDto.getDoctorName()+" on "+appointmentRegistrationDto.getFacilityName(),HttpStatus.BAD_REQUEST);
        }
        Doctor doctor = doctorService.findByDoctorName(appointmentRegistrationDto.getDoctorName());
        Optional<Hospital> hospital = hospitalService.getHospitalByDoctorName(appointmentRegistrationDto.getDoctorName());
        MedicalFacilities facility = facilitiesService.findByFacilityName(appointmentRegistrationDto.getFacilityName());
        Optional<AppUser> appUser = appUserServices.findByUserEmail(appointmentRegistrationDto.getAppUserEmail());
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
    @Cacheable(value = "AllPendingAppointmentsByDoctorEmail", key = "#doctorEmail",unless = "#result==null")
    public List<PendingAppointmentProjection> getPendingAppointmentsByDoctorEmail(String doctorEmail) {
        List<PendingAppointmentProjection> appointmentPendinglList = appointmentRepo.findPendingAppointmentsByDoctorEmail(doctorEmail);
        if (appointmentPendinglList.isEmpty()) throw new AppointmentException("You have not any appointment till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentPendinglList;
    }

    @Override
    @Cacheable(value = "AllUpComingAppointmentsByDoctorEmail", key = "#doctorEmail",unless = "#result==null")
    public List<UpComingAppointmentProjection> getUpComingAppointmentsByDoctorEmail(String doctorEmail) {
        List<UpComingAppointmentProjection> appointmentUpCominglList = appointmentRepo.findUpComingAppointmentsByDoctorEmail(doctorEmail);
        if (appointmentUpCominglList.isEmpty()) throw new AppointmentException("There are not any upcoming appointment till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentUpCominglList;
    }

    @Override
    @Cacheable(value = "AllCompletedAppointmentsByDoctorEmail", key = "#doctorEmail",unless = "#result==null")
    public List<CompletedAppointmentProjection> getCompletedAppointmentsByDoctorEmail(String doctorEmail) {
        List<CompletedAppointmentProjection> appointmentCompletelList = appointmentRepo.findCompletedAppointmentsByDoctorEmail(doctorEmail);
        if (appointmentCompletelList.isEmpty()) throw new AppointmentException("No appointment record available till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentCompletelList;
    }

    @Override
    @Cacheable(value = "AllCancelAppointmentsByDoctorEmail", key = "#doctorEmail",unless = "#result==null")
    public List<CancelAppointmentProjection> getCancelAppointmentsByDoctorEmail(String doctorEmail) throws AppointmentException {
        List<CancelAppointmentProjection> appointmentCancelList = appointmentRepo.findCancelAppointmentsByDoctorEmail(doctorEmail);
        if (appointmentCancelList.isEmpty()) throw new AppointmentException("No appointment record available till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentCancelList;
    }

    @Override
    @Cacheable(value = "AllBookedAppointmentByUserEmail", key = "#userEmail",unless = "#result==null")
    public List<AppUserAppointmentProjection> findAllBookedAppointmentByUserEmail(String userEmail) throws AppointmentException {
        List<AppUserAppointmentProjection> appointmentList = appointmentRepo.findAllAppointmentListByUserEmail(userEmail);
        if (appointmentList.isEmpty()) throw new AppointmentException("No appointment record available till "+ LocalDate.now(), HttpStatus.NOT_FOUND);
        return appointmentList;
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "AllPendingAppointmentsByDoctorEmail", allEntries = true),
                    @CacheEvict(value = "AllUpComingAppointmentsByDoctorEmail", allEntries = true),
                    @CacheEvict(value = "AllCompletedAppointmentsByDoctorEmail", allEntries = true),
                    @CacheEvict(value = "AllCancelAppointmentsByDoctorEmail", allEntries = true),
                    @CacheEvict(value = "AllBookedAppointmentByUserEmail", key = "#userEmail")
            }
    )
    public String removeAppointmentsByUserEmail(String userEmail) {
        appointmentRepo.deleteAppointmentByAppUserEmail(userEmail);
        return "User with "+ userEmail +" has removed SuccessFully";
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "AllPendingAppointmentsByDoctorEmail", allEntries = true),
                    @CacheEvict(value = "AllUpComingAppointmentsByDoctorEmail", allEntries = true),
                    @CacheEvict(value = "AllCompletedAppointmentsByDoctorEmail", allEntries = true),
                    @CacheEvict(value = "AllCancelAppointmentsByDoctorEmail", allEntries = true),
                    @CacheEvict(value = "AllBookedAppointmentByUserEmail", key = "#userEmail")
            }
    )

    public String updateAppointmentStatus(String userEmail, String newStatus, String facility) {
        appointmentRepo.updateAppointmentStatusByUserEmail(userEmail,newStatus,LocalDateTime.now(),facility);
        return "User with "+ userEmail +"'s status has been updated to "+newStatus+ " for "+facility;
    }
}
