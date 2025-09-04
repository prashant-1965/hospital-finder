package com.healthcare.finder.doctorHospitalFinder.application.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private int userAge;
    private String userGender;
    private String userMobile;
    private String userEmail;
    private String userCountry;
    private String userState;
    private String userPassword;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<GlobalReview> reviews;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
}

