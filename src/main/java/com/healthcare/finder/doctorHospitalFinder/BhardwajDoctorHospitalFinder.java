package com.healthcare.finder.doctorHospitalFinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
@EnableCaching
public class BhardwajDoctorHospitalFinder {

	public static void main(String[] args) {
		SpringApplication.run(BhardwajDoctorHospitalFinder.class, args);
	}
}
