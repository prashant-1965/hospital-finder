package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.Appointment;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CancelAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CompletedAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.PendingAppointmentProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.UpComingAppointmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
//    List<PendingAppointmentProjection> findPendingAppointmentsByDoctorName(@Param("doctorName") String doctorName, @Param("confirmed") String pending);
//    List<UpComingAppointmentProjection> findUpComingAppointmentsByDoctorName(@Param("doctorName") String doctorName, @Param("pending") String confirmed);
//    List<CompletedAppointmentProjection> findCompletedAppointmentsByDoctorName(@Param("doctorName") String doctorName, @Param("confirmed") String completed);
//    List<CancelAppointmentProjection> findCancelAppointmentsByDoctorName(@Param("doctorName") String doctorName, @Param("confirmed") String cancelled);
}
