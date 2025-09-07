package com.healthcare.finder.doctorHospitalFinder.application.projection;

import com.healthcare.finder.doctorHospitalFinder.application.entity.MedicalFacilities;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class DoctorApplicationProjection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
    private List<String> medicalFacilities;
}
