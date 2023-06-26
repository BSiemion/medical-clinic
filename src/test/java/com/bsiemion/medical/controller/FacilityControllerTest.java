package com.bsiemion.medical.controller;

import com.bsiemion.medical.TestDataFactory;
import com.bsiemion.medical.model.entity.Doctor;
import com.bsiemion.medical.model.entity.Facility;
import com.bsiemion.medical.repository.DoctorRepository;
import com.bsiemion.medical.repository.FacilityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class FacilityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    FacilityRepository facilityRepository;
    @Autowired
    DoctorRepository doctorRepository;

    @Test
    void getAllFacilities_Returned() throws Exception {
        Facility facility = TestDataFactory.createFacility("test");
        facilityRepository.save(facility);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/facilities"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("test"));
    }

    @Test
    void getAllDoctorsFromFacility() throws Exception {
        Facility facility = TestDataFactory.createFacility("test2");
        facilityRepository.save(facility);
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(TestDataFactory.createDoctor("test1", 1L));
        doctors.add(TestDataFactory.createDoctor("test2", 2L));
        doctors.add(TestDataFactory.createDoctor("test3", 3L));
        doctors.stream()
                        .forEach(doctor -> doctor.setFacilities(Collections.singletonList(facility)));
        doctorRepository.saveAll(doctors);
        facility.getDoctors().addAll(doctors);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/facilities/{facilityName}", "test2"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("test1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("test2"));
    }
}
