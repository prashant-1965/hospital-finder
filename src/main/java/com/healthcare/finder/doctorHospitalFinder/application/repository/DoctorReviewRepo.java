package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.DoctorReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorReviewRepo extends JpaRepository<DoctorReview,Long> {

    @Query("select count(dr) from DoctorReview dr join dr.doctor d where d.doctorName = :name")
    long getTotalReviewForIndividualDoctorByName(@Param("name") String name);
}
