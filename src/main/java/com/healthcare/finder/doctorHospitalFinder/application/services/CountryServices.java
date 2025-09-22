package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.CountryRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.Country;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CountryListProjection;

import java.util.List;

public interface CountryServices {
    List<CountryListProjection> getCountryList();
    String addCountry(CountryRegisterDto countryRegisterDto);
    Country findCountryByName(String countryName);
}
