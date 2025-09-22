package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.CountryException;
import com.healthcare.finder.doctorHospitalFinder.application.classException.StateException;
import com.healthcare.finder.doctorHospitalFinder.application.dto.StateRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.Country;
import com.healthcare.finder.doctorHospitalFinder.application.entity.State;
import com.healthcare.finder.doctorHospitalFinder.application.repository.StatesRepo;
import com.healthcare.finder.doctorHospitalFinder.application.projection.StateListProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceImpl implements StateService{

    @Autowired
    private StatesRepo statesRepo;
    @Autowired
    private CountryServices countryServices;

    @Override
    @Cacheable(value = "StateListProjection",key = "#countryName",unless = "#result==null")
    public List<StateListProjection> getStateList(String countryName) throws StateException {
        List<StateListProjection> stateListProjections = statesRepo.allStateListByCountry(countryName);
        if(stateListProjections.isEmpty()){
            throw new StateException("No state found in "+countryName, HttpStatus.NOT_FOUND);
        }
        return stateListProjections.stream().sorted().toList();
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "State", allEntries = true),
                    @CacheEvict(value = "StateListProjection", allEntries = true)
            }
    )
    public String addState(StateRegisterDto stateRegisterDto) throws StateException,CountryException {
        if(stateRegisterDto.getStateName().isEmpty()){
            throw new StateException("Invalid State Name!",HttpStatus.BAD_REQUEST);
        }
        Country country = countryServices.findCountryByName(stateRegisterDto.getCountryName());
        if(country==null){
            throw new CountryException("Our Facility is not available in "+stateRegisterDto.getCountryName(),HttpStatus.BAD_REQUEST);
        }
        State isExist = this.findByStateName(stateRegisterDto.getStateName());
        if(isExist!=null){
            throw new StateException("State Already Exist",HttpStatus.BAD_REQUEST);
        }
        State state = new State();
        state.setStateName(stateRegisterDto.getStateName());
        state.setCountry(country);
        statesRepo.save(state);
        return "State added SuccessFully";
    }

    @Override
    @Cacheable(value = "State",key = "#stateName",unless = "#result==null")
    public State findByStateName(String stateName) {
        return statesRepo.findByStateName(stateName);
    }
}
