package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.MedicalFacilities;
import com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilitiesRepo extends JpaRepository<MedicalFacilities,Long> {

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection("+
            "ms.facilityName, ms.facilityDescription) "+
            "from MedicalFacilities ms")
    List<FacilityListProjection> getAllAvailableFacility();

    @Query("select f from MedicalFacilities f where f.facilityName = :name")
    MedicalFacilities findByFacilityName(String name);

//    @Query("select a from AppUser a")
//    Optional<List<String>> medicalFacilityExistByDoctorNameAndHospitalName(@Param("hospitalName") String hospitalName, @Param("doctorName") String doctorName);
}
