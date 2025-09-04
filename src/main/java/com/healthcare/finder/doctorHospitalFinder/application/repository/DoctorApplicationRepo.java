package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.DoctorApplication;
import com.healthcare.finder.doctorHospitalFinder.application.projection.DoctorApplicationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorApplicationRepo extends JpaRepository<DoctorApplication,Long> {
    @Query("select da from DoctorApplication da")
    List<DoctorApplication> getAllPendingDoctors();

    @Query("select da from DoctorApplication da where da.tmpDoctorEmail = :email")
    Optional<DoctorApplication> getDoctorNameByEmail(@Param("email") String email);

    @Modifying
    @Query("delete from DoctorApplication da where da.tmpDoctorEmail = :email")
    void deleteDoctorApplicationByEmail(@Param("email") String email);
}
