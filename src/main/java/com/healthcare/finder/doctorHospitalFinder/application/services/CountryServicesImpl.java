package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.CountryException;
import com.healthcare.finder.doctorHospitalFinder.application.dto.CountryRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.Country;
import com.healthcare.finder.doctorHospitalFinder.application.repository.CountryRepo;
import com.healthcare.finder.doctorHospitalFinder.application.projection.CountryListProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServicesImpl implements CountryServices {

    @Autowired
    private CountryRepo countryRepo;

    @Override
    public List<CountryListProjection> getCountryList() throws CountryException {
        List<CountryListProjection> countryListProjections = countryRepo.allCountryList();
        if(countryListProjections.isEmpty()){
            throw new CountryException("No country Found", HttpStatus.NOT_FOUND);
        }
        return countryListProjections.stream().sorted().toList();
    }

    @Override
    public String addCountry(CountryRegisterDto countryRegisterDto) throws CountryException {
        if(countryRegisterDto.getCountryName().isEmpty()){
            throw new CountryException("Invalid Country Name!",HttpStatus.BAD_REQUEST);
        }
        Country isExist = countryRepo.findCountryByName(countryRegisterDto.getCountryName());
        if(isExist!=null){
            throw new CountryException("Country Already Exist",HttpStatus.BAD_REQUEST);
        }
        Country country = new Country();
        country.setCountryName(countryRegisterDto.getCountryName());
        countryRepo.save(country);
        return "Country Added SuccessFully";
    }
}
