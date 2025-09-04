package com.healthcare.finder.doctorHospitalFinder.application.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Data
public class HospitalApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tempHospitalId;

    private String tempHospitalName;
    private String tempHospitalType;
    private int tempHospitalYearOfEstablishment;
    private int tempHospitalNumOfUsersServed;
    private double tempHospitalRating;
    private String tempHospitalContact;
    private String tempHospitalAddress;
    private String tempCountryName;
    private String tempStateName;

    @ManyToMany
    @JoinTable(
            name = "temp_hospital_facility",
            joinColumns = @JoinColumn(name = "temp_hospital_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<MedicalFacilities> facilities;
}
