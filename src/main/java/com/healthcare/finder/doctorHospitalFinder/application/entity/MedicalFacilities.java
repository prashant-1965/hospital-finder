package com.healthcare.finder.doctorHospitalFinder.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class MedicalFacilities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;

    private String facilityName;
    private String facilityDescription;

    @OneToMany(mappedBy = "facilities",cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @ManyToMany(mappedBy = "facilities")
    private List<Doctor> doctors;

    @ManyToMany(mappedBy = "facilities")
    private List<Hospital> hospitals;

    @ManyToMany(mappedBy = "medicalFacilities")
    private List<DoctorApplication> doctorApplications;
}

