package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import com.healthcare.finder.doctorHospitalFinder.application.projection.AppUserCountryStateProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.AppUserCountryStateProjection("+
            "au.userCountry,au.userState) "+
            "from AppUser au where au.userEmail = :email")
    List<AppUserCountryStateProjection> getAppUserCountryAndState(@Param("email") String email);

    @Query("select a from AppUser a where a.userMobile = :mobile")
    Optional<AppUser> findByUserMobile(@Param("mobile") String mobile);

    @Query("select a from AppUser a where a.userEmail = :email")
    Optional<AppUser> findByUserEmail(@Param("email") String email);

    @Query("select a from AppUser a where a.userName = :name")
    Optional<AppUser> findByUsername(@Param("name") String name);

}
