package com.bsiemion.medical.controller;

import com.bsiemion.medical.TestDataFactory;
import com.bsiemion.medical.model.dto.DoctorCreationDto;
import com.bsiemion.medical.model.entity.Doctor;
import com.bsiemion.medical.model.entity.Facility;
import com.bsiemion.medical.repository.DoctorRepository;
import com.bsiemion.medical.repository.FacilityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    FacilityRepository facilityRepository;

    @Test
    void getAllDoctors_Returned() throws Exception {
        Doctor doctor = TestDataFactory.createDoctor("test", 1L);
        doctorRepository.save(doctor);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/doctors"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("test"));
    }

    @Test
    void addDoctor_Returned() throws Exception {
        DoctorCreationDto doctorCreationDto = TestDataFactory.createDoctorCreationDto("test3");
        String doctorJson = mapper.writeValueAsString(doctorCreationDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(doctorJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test3"));
    }

    @Test
    void removeDoctor_Returned() throws Exception {
        Doctor doctor = TestDataFactory.createDoctor("test", 2L);
        doctorRepository.save(doctor);
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/doctors/{email}", "test"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test"));
    }

    @Test
    void addFacilityToDoctor_Returned() throws Exception {
        Doctor doctor = TestDataFactory.createDoctor("test4", 3L);
        Facility facility = TestDataFactory.createFacility("testName");
        doctorRepository.save(doctor);
        facilityRepository.save(facility);
        this.mockMvc
                .perform(MockMvcRequestBuilders.patch("/doctors/{email}/assign/{facilityName}", "test4", "testName"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.facilityIds").value("1"));
    }
}
