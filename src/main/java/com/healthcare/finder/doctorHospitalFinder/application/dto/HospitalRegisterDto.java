package com.healthcare.finder.doctorHospitalFinder.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class HospitalRegisterDto {
    private String hospitalName;
    private String hospitalType;
    private int hospitalYearOfEstablishment;
    private int hospitalNumOfUsersServed;
    private String hospitalContact;
    private String hospitalAddress;
    private String countryName;
    private String stateName;
    private List<String> facilities;
}
