package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.State;
import com.healthcare.finder.doctorHospitalFinder.application.projection.StateListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatesRepo extends JpaRepository<State, Long> {

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.StateListProjection(s.stateName) " +
            "from State s join s.country c " +
            "where c.countryName = :countryName")
    List<StateListProjection> allStateListByCountry(@Param("countryName") String countryName);

    @Query("select s from State s where s.stateName = :name")
    State findByStateName(String name);
}