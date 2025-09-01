package com.healthcare.finder.doctorHospitalFinder.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalId;

    private String hospitalName;
    private String hospitalType;
    private int hospitalYearOfEstablishment;
    private int hospitalNumOfUsersServed;
    private double hospitalRating;
    private String hospitalContact;
    private String hospitalAddress;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @OneToMany(mappedBy = "hospital",cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<Doctor> doctors;

    @ManyToMany
    @JoinTable(
            name = "hospital_service",
            joinColumns = @JoinColumn(name = "hospital_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<MedicalFacilities> facilities;
}

