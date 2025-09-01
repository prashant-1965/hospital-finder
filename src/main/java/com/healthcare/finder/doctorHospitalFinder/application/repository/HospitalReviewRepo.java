package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.HospitalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalReviewRepo extends JpaRepository<HospitalReview,Long> {
    @Query("select count(hr) from HospitalReview hr join hr.hospital h where h.hospitalName = :name")
    long getTotalReviewForIndividualHospitalByName(@Param("name") String name);
}
