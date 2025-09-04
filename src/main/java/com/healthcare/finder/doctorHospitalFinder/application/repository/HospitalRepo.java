package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.Hospital;
import com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualHospitalDetailProjection;
import com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepo extends JpaRepository<Hospital,Long> {

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection("+
    "h.hospitalName, h.hospitalType, h.hospitalRating, h.hospitalAddress) "+
    "from Hospital h join h.country c where c.countryName = :countryName order by h.hospitalRating desc")
    List<TopNHospitalListProjection> getFirstNHospitalListByCountry(@Param("countryName") String countryName, Pageable pageable);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection("+
            "h.hospitalName, h.hospitalType, h.hospitalRating, h.hospitalAddress) "+
            "from Hospital h order by h.hospitalRating desc")
    List<TopNHospitalListProjection> getFirstNHospitalList(Pageable pageable);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection("+
            "h.hospitalName, h.hospitalType, h.hospitalRating, h.hospitalAddress) "+
            "from Hospital h join h.country c join h.facilities f "+
            "where c.countryName = :countryName and h.hospitalType='gov' and f.facilityName = :facilityIn "+
            "order by h.hospitalRating desc")
    List<TopNHospitalListProjection> getFirstNGovHospitalList(@Param("countryName") String countryName, @Param("facilityIn") String facilityIn);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.TopNHospitalListProjection("+
            "h.hospitalName, h.hospitalType, h.hospitalRating, h.hospitalAddress) "+
            "from Hospital h join h.country c join h.facilities f "+
            "where c.countryName = :countryName and h.hospitalType='private' and f.facilityName = :facilityIn "+
            "order by h.hospitalRating desc")
    List<TopNHospitalListProjection> getFirstNPrivateHospitalList(@Param("countryName") String countryName, @Param("facilityIn") String facilityIn);

    @Query("select h from Hospital h where h.hospitalName = :name")
    Hospital findByHospitalName(@Param("name") String name);

    @Query("select h.hospitalRating from Hospital h where h.hospitalName = :name")
    double getRattingByHospitalName(@Param("name") String name);

    @Query("select h.hospitalName from Hospital h")
    List<String> findAllHospital();

    @Query("select h from Doctor d join d.hospital h where d.doctorName = :doctorName")
    Optional<Hospital> getHospitalByDoctorName(@Param("doctorName") String doctorName);

    @Query("select h.hospitalName from Doctor d join d.hospital h where d.doctorEmail = :doctorEmail")
    Optional<String> getHospitalByDoctorEmail(@Param("doctorName") String doctorEmail);

    @Query("select h.hospitalName from Hospital h join h.facilities f where f.facilityName = :facilityName order by h.hospitalName")
    List<String> getHospitalByFacilityName(@Param("facilityName") String facilityName);

    @Query("select h.hospitalName from Doctor d join d.hospital h join d.facilities f where d.doctorEmail = :doctorEmail and f.facilityName = :facilityName order by h.hospitalName desc")
    List<String> getHospitalByFacilityNameAndDoctorEmail(@Param("facilityName") String facilityName,@Param("doctorName") String doctorEmail);

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.IndividualHospitalDetailProjection( "+
            "h.hospitalName,h.hospitalType,h.hospitalYearOfEstablishment,h.hospitalNumOfUsersServed,h.hospitalRating,h.hospitalContact,h.hospitalAddress) "+
            " from Hospital h where h.hospitalName = :hospitalName")
    Optional<IndividualHospitalDetailProjection> getHospitalDetailByName(@Param("hospitalName") String hospitalName);

}
