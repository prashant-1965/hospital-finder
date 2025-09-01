package com.healthcare.finder.doctorHospitalFinder.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessUrl implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        switch (role) {
            case "ROLE_DOCTOR" -> response.sendRedirect("/doctorHome");
            case "ROLE_PATIENT" -> response.sendRedirect("/patientHome");
            case "ROLE_ADMIN" -> response.sendRedirect("/adminHome");
            default -> response.sendRedirect("/login");
        }
    }
}
