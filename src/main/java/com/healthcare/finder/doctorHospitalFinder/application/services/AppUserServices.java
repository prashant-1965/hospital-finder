package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.AppUserRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.AppUserCountryStateProjection;

import java.util.List;

public interface AppUserServices {
    String addAppUser(AppUserRegisterDto appUserRegisterDto);
    String changeAppUserPasswordRequest(String userEmail, String password);

}
