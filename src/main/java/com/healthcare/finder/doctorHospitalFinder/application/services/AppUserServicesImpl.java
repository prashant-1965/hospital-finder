package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.classException.AppUserException;
import com.healthcare.finder.doctorHospitalFinder.application.dto.AppUserRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.entity.Role;
import com.healthcare.finder.doctorHospitalFinder.application.repository.AppUserRepo;
import com.healthcare.finder.doctorHospitalFinder.application.projection.AppUserCountryStateProjection;
import com.healthcare.finder.doctorHospitalFinder.application.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserServicesImpl implements AppUserServices, UserDetailsService {

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "loadUserByUsername",allEntries = true),
                    @CacheEvict(value = "AppUser",allEntries = true)
            }
    )
    public String addAppUser(AppUserRegisterDto appUserRegisterDto) throws AppUserException {
        if(appUserRegisterDto.getUserCountry().isEmpty()  || appUserRegisterDto.getUserEmail().isEmpty()){
            throw new AppUserException("All fields are mandatory to fill",HttpStatus.BAD_REQUEST);
        }
        Optional<Role> role = roleRepo.getByRoleName(appUserRegisterDto.getRole());
        if(role.isEmpty()){
            throw new AppUserException("Invalid Role",HttpStatus.BAD_REQUEST);
        }
        if(appUserRepo.findByUserMobile(appUserRegisterDto.getUserMobile()).isPresent()){
            throw new AppUserException("Mobile number already exist!",HttpStatus.BAD_REQUEST);
        }
        if(appUserRepo.findByUserEmail(appUserRegisterDto.getUserEmail()).isPresent()){
            throw new AppUserException("Email already exist!",HttpStatus.BAD_REQUEST);
        }

        AppUser appUser = new AppUser();
        appUser.setUserName(appUserRegisterDto.getUserName());
        appUser.setUserMobile(appUserRegisterDto.getUserMobile());
        appUser.setUserAge(appUserRegisterDto.getUserAge());
        appUser.setUserCountry(appUserRegisterDto.getUserCountry());
        appUser.setUserEmail(appUserRegisterDto.getUserEmail());
        appUser.setUserGender(appUserRegisterDto.getUserGender());
        appUser.setUserState(appUserRegisterDto.getUserState());
        String encodePassword = passwordEncoder.encode(appUserRegisterDto.getPassword());
        appUser.setUserPassword(encodePassword);
        appUser.setRole(role.get());
        appUserRepo.save(appUser);
        return  "Account Created Successfully";
    }

    @Override
    @Cacheable(value = "loadUserByUsername",key = "#userEmail",unless = "#result==null")
    public UserDetails loadUserByUsername(String userEmail) throws AppUserException
    {
        Optional<AppUser> appUser = this.findByUserEmail(userEmail);
        if(appUser.isEmpty()){
            throw new AppUserException("Invalid login Details",HttpStatus.NOT_FOUND);
        }
        return new User(
                appUser.get().getUserEmail(),
                appUser.get().getUserPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_"+appUser.get().getRole().getRoleName()))
        );
    }
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "loadUserByUsername",allEntries = true),
                    @CacheEvict(value = "AppUser",allEntries = true)
            }
    )
    public String changeAppUserPasswordRequest(String userEmail,String password) {
        Optional<AppUser> appUser = this.findByUserEmail(userEmail);
        if(appUser.isEmpty()){
            throw new AppUserException(userEmail+" has not registered in our system!",HttpStatus.NOT_FOUND);
        }
        appUserRepo.updateUserPassword(userEmail,passwordEncoder.encode(password));
        return "You Password Updated SuccessFully!";
    }

    @Override
    @Cacheable(value = "AppUser",key = "#Email",unless = "#result==null")
    public Optional<AppUser> findByUserEmail(String Email) {
        return appUserRepo.findByUserEmail(Email);
    }
}
