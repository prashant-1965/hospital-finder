package com.healthcare.finder.doctorHospitalFinder.application.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Commit
public class CustomisationFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCountryListTest200() throws Exception{
        mockMvc.perform(get("/myCustom/getCountryList")).andExpect(status().isOk());
    }
}
