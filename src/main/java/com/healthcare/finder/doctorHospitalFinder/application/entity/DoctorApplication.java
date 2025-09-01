package com.healthcare.finder.doctorHospitalFinder.application.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class DoctorApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tmpDoctorId;

    private String tmpDoctorName;
    private int tmpDoctorAge;
    private String tmpDoctorGender;
    private int tmpDoctorYearsOfExperience;
    private String tmpDoctorGraduateCollege;
    private String tmpDoctorFieldOfExpertise;
    private String tmpDoctorEmail;
    private String tmpDoctorMobile;
    private String tmpDoctorDetailAddress;
    private String tmpDoctorType; // Gov , Private
    private String hospitalAppliedFor;
    private String tmpDoctorCountryName;
    private String tmpDoctorStateName;
    @ManyToMany
    @JoinTable(
            name = "temp_doctor_facility",
            joinColumns = @JoinColumn(name = "temp_doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<MedicalFacilities> medicalFacilities;
}
