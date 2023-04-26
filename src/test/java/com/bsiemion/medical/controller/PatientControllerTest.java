package com.bsiemion.medical.controller;

import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.repozitory.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PatientRepository patientRepository;
    @Test
    void addPatient_Returned() throws Exception {
        Patient patient = createPatient("test");
        String patientJson = mapper.writeValueAsString(patient);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test"));
    }
    @Test
    void removePatient_Returned() throws Exception {
        Patient patient = createPatient("test");
        patientRepository.save(patient);
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/patients/{email}","test"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test"));
    }
    @Test
    void editPatient_Returned() throws Exception {
        Patient patient = createPatient("test");
        patientRepository.save(patient);
        Patient editInfo = Patient.builder()
                .email("test")
                .id(1L)
                .birthday(LocalDate.of(2000, 10, 10))
                .firstName("testName")
                .idCardNo("1352523523")
                .lastName("Wariacie")
                .password("testPass")
                .phoneNumber("1235436")
                .build();
        String editInfoJson = mapper.writeValueAsString(editInfo);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/patients/{email}","test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editInfoJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Wariacie"));
    }
    @Test
    void editPassword_MessageReturned() throws Exception {
        Patient patient = createPatient("test5");
        patientRepository.save(patient);

        String password = "12345";
        this.mockMvc
                .perform(MockMvcRequestBuilders.patch("/patients/{email}","test5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(password))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Password changed successfully"));
    }
    @Test
    void getAllPatients_Returned() throws Exception {
        Patient patient = createPatient("test");
        patientRepository.save(patient);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/patients"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("test"));
    }
    @Test
    void getPatient_Returned() throws Exception {
        Patient patient = createPatient("test");
        patientRepository.save(patient);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/patients/{email}","test"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test"));
    }
    private static Patient createPatient(String email) {
        return Patient.builder()
                .email(email)
                .id(1L)
                .birthday(LocalDate.of(2000, 10, 10))
                .firstName("testName")
                .idCardNo("1352523523")
                .lastName("testLastName")
                .password("testPass")
                .phoneNumber("1235436")
                .build();
    }
}
