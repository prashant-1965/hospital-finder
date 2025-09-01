package com.healthcare.finder.doctorHospitalFinder.application.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countryId;

    @Column(name = "country_name",nullable = false)
    private String countryName;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<State> states;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<Doctor> doctors;

    @OneToMany(mappedBy = "country",cascade = CascadeType.ALL)
    private List<Hospital> hospitals;
}
