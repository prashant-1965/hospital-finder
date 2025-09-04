package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.MedicalFacilities;
import com.healthcare.finder.doctorHospitalFinder.application.projection.FacilityListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("select f.facilityName from Doctor d join d.facilities f where d.doctorEmail = :doctorEmail order by f.facilityName desc")
    List<String> findFacilityByDoctorEmail(@Param("doctorEmail") String doctorEmail);

    @Query("select f.facilityName from Hospital h join h.facilities f where h.hospitalName = :hospitalName order by h.hospitalName desc")
    List<String> getFacilityByHospitalName(@Param("hospitalName") String hospitalName);

}
