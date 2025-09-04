package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.HospitalApplication;
import com.healthcare.finder.doctorHospitalFinder.application.projection.HospitalApplicationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalApplicationRepo extends JpaRepository<HospitalApplication,Long> {
    @Query("select ha from HospitalApplication ha")
    List<HospitalApplication> getAllPendingHospitalApplicationList();

    @Query("select ha from HospitalApplication ha where ha.tempHospitalName = :hospitalName")
    HospitalApplication getByHospitalName(@Param("hospitalName") String hospitalName);

    @Modifying
    @Query("delete from HospitalApplication ha where ha.tempHospitalName = :hospitalName")
    void deleteHospitalApplicationByName(@Param("hospitalName") String hospitalName);
}
