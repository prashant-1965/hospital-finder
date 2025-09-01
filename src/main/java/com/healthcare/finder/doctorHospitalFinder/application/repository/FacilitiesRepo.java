package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.MedicalFacilities;
import com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilitiesRepo extends JpaRepository<MedicalFacilities,Long> {

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection("+
            "ms.facilityName, ms.facilityDescription) "+
            "from MedicalFacilities ms")
    List<FacilityListProjection> getAllAvailableFacility();

    @Query("select f from MedicalFacilities f where f.facilityName = :name")
    MedicalFacilities findByFacilityName(String name);
}
