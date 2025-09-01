package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.Country;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CountryListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepo extends JpaRepository<Country, Long> {

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.CountryListProjection("+
    "c.countryName) "+
    "from Country c")
    List<CountryListProjection> allCountryList();

    @Query("select c from Country c where c.countryName = :name")
    Country findCountryByName(String name);
}
