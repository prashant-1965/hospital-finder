package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.Doctor;
import com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualDoctorDetailProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection("+
            "d.doctorName, d.doctorYearsOfExperience, d.doctorRating, d.doctorDetailAddress) "+
            "from Doctor d join d.country c where c.countryName = :countryName order by d.doctorRating desc")
    List<TopNDoctorListProjection> findTopNDoctorsByCountry(@Param("countryName") String countryName, Pageable pageable);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection("+
            "d.doctorName, d.doctorYearsOfExperience, d.doctorRating, d.doctorDetailAddress) "+
            "from Doctor d order by d.doctorRating desc")
    List<TopNDoctorListProjection> findTop10Doctors(Pageable pageable);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection("+
            "d.doctorName, d.doctorYearsOfExperience, d.doctorRating, d.doctorDetailAddress) "+
            "from Doctor d join d.country c join d.facilities f "+
            "where d.doctorType = 'gov' and c.countryName = :countryName and f.facilityName = :facilityIn "+
            "order by d.doctorRating desc")
    List<TopNDoctorListProjection> findTopNGovDoctorsAsFacility(@Param("countryName") String countryName, @Param("facilityIn") String facilityIn);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection("+
            "d.doctorName, d.doctorYearsOfExperience, d.doctorRating, d.doctorDetailAddress) "+
            "from Doctor d join d.country c join d.facilities f "+
            "where d.doctorType = 'private' and c.countryName = :countryName and f.facilityName = :facilityIn "+
            "order by d.doctorRating desc")
    List<TopNDoctorListProjection> findTopNPrivateDoctorsWithFacility(@Param("countryName") String countryName, @Param("facilityIn") String facilityIn);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection("+
            "d.doctorName, d.doctorYearsOfExperience, d.doctorRating,d.doctorDetailAddress) "+
            "from Doctor d join d.country c join d.facilities f "+
            "where d.doctorType = 'private' and c.countryName = :countryName and f.facilityName = :facilityIn "+
            "order by d.doctorYearsOfExperience desc")
    List<TopNDoctorListProjection> findTopNPrivateExperiencedDoctorWithSpecialisation(@Param("countryName") String countryName, @Param("facilityIn") String facilityIn);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection("+
            "d.doctorName, d.doctorYearsOfExperience, d.doctorRating,d.doctorDetailAddress) "+
            "from Doctor d join d.country c join d.facilities f "+
            "where d.doctorType = 'private' and c.countryName = :countryName and f.facilityName = :facilityIn "+
            "order by d.doctorYearsOfExperience")
    List<TopNDoctorListProjection> findLeastNPrivateExperiencedDoctorWithSpecialisation(@Param("countryName") String countryName, @Param("facilityIn") String facilityIn);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection("+
            "d.doctorName, d.doctorYearsOfExperience, d.doctorRating, d.doctorDetailAddress) "+
            "from Doctor d join d.country c join d.facilities f "+
            "where d.doctorType = 'gov' and c.countryName = :countryName and f.facilityName = :facilityIn "+
            "order by d.doctorYearsOfExperience desc")
    List<TopNDoctorListProjection> findTopNGovExperiencedDoctorWithSpecialisation(@Param("countryName") String countryName, @Param("facilityIn") String facilityIn);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNDoctorListProjection("+
            "d.doctorName, d.doctorYearsOfExperience, d.doctorRating, d.doctorDetailAddress) "+
            "from Doctor d join d.country c join d.facilities f "+
            "where d.doctorType = 'gov' and c.countryName = :countryName and f.facilityName = :facilityIn "+
            "order by d.doctorYearsOfExperience")
    List<TopNDoctorListProjection> findLeastNGovExperiencedDoctorWithSpecialisation(@Param("countryName") String countryName, @Param("facilityIn") String facilityIn);

    @Query("select d from Doctor d where d.doctorName = :name")
    Doctor findByDoctorName(@Param("name") String name);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualDoctorDetailProjection(" +
            "d.doctorName, d.doctorAge, d.doctorGender, d.doctorEmail, d.doctorYearsOfExperience, d.doctorRating, d.doctorGraduateCollege, d.doctorFieldOfExpertise, d.doctorType) " +
            "from Doctor d where d.doctorName = :doctorName")
    IndividualDoctorDetailProjection findDetailsByDoctorName(@Param("doctorName") String doctorName);

    @Query("select d.doctorRating from Doctor d where d.doctorName = :name")
    Double getAvgRattingByDoctorName(@Param("name") String name);

    @Query("select d.doctorName from Doctor d join d.hospital h join d.facilities f where h.hospitalName = :hospitalName and f.facilityName = :facilityName order by d.doctorName")
    List<String> getDoctorByFacilityAndHospital(@Param("hospitalName") String hospitalName, @Param("facilityName") String facilityName);

    @Query("select d.doctorName from Doctor d")
    List<String> findAllDoctors();
}
