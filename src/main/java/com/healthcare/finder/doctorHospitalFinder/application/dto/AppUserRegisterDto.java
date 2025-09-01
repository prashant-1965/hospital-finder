package com.healthcare.finder.doctorHospitalFinder.application.dto;

import lombok.Data;

@Data
public class AppUserRegisterDto {
    private String userName;
    private int userAge;
    private String userGender;
    private String userMobile;
    private String userEmail;
    private String userCountry;
    private String userState;
    private String role;
    private String password;
}
