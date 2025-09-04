package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.Appointment;
import com.healthcare.finder.doctorHospitalFinder.application.projection.*;
import com.healthcare.finder.doctorHospitalFinder.application.projection.UpComingAppointmentProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Long> {

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.PendingAppointmentProjection("
            +"u.userName,u.userEmail,u.userMobile, a.appointmentStatus,a.appointmentDate,a.appointmentAppliedDate) "
            +"from Appointment a join a.user u join a.doctor d where d.doctorEmail = :doctorEmail and a.appointmentStatus = 'pending' "
            +"order by u.userName desc")
    List<PendingAppointmentProjection> findPendingAppointmentsByDoctorEmail(@Param("doctorEmail") String doctorEmail);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.UpComingAppointmentProjection("
            +"u.userName,u.userEmail,u.userMobile, a.appointmentStatus,a.appointmentDate,a.appointmentAppliedDate) "
            +"from Appointment a join a.user u join a.doctor d where d.doctorEmail = :doctorEmail and a.appointmentStatus = 'upComing' "
            +"order by u.userName desc")
    List<UpComingAppointmentProjection> findUpComingAppointmentsByDoctorEmail(@Param("doctorEmail") String doctorEmail);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.CompletedAppointmentProjection("
            +"u.userName,u.userEmail, a.appointmentStatus,a.appointmentDate,a.appointmentAppliedDate) "
            +"from Appointment a join a.user u join a.doctor d where d.doctorEmail = :doctorEmail and a.appointmentStatus = 'completed' "
            +"order by u.userName desc")
    List<CompletedAppointmentProjection> findCompletedAppointmentsByDoctorEmail(@Param("doctorEmail") String doctorEmail);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.CancelAppointmentProjection("
            +"u.userName,u.userEmail, a.appointmentStatus,a.appointmentDate,a.appointmentAppliedDate) "
            +"from Appointment a join a.user u join a.doctor d where d.doctorEmail = :doctorEmail and a.appointmentStatus = 'cancelled' "
            +"order by u.userName desc")
    List<CancelAppointmentProjection> findCancelAppointmentsByDoctorEmail(@Param("doctorEmail") String doctorEmail);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.AppUserAppointmentProjection("
            +"d.doctorName, h.hospitalName, a.appointmentStatus,a.appointmentAppliedDate) "
            +"from Appointment a join a.user u join a.hospital h join a.doctor d where u.userEmail = :userEmail "
            +"order by d.doctorName desc")
    List<AppUserAppointmentProjection> findAllAppointmentListByUserEmail(@Param("userEmail") String userEmail);

    @Modifying
    @Transactional
    @Query("delete from Appointment a where a.user.userEmail = :email")
    void deleteAppointmentByAppUserEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("update Appointment a "
            +"set a.appointmentStatus = :status, a.appointmentDate = :localDateTime "
            + "where a.user.userEmail = :email")
    void updateAppointmentStatusByUserEmail(@Param("email") String email, @Param("status") String status, @Param("localDateTime")LocalDateTime localDateTime);
}
