package com.bsiemion.medical.controller;

import com.bsiemion.medical.TestDataFactory;
import com.bsiemion.medical.mapper.PatientMapper;
import com.bsiemion.medical.model.dto.VisitCreationDto;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.model.entity.Visit;
import com.bsiemion.medical.repository.PatientRepository;
import com.bsiemion.medical.repository.VisitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PatientMapper patientMapper;
    @BeforeEach
    void setUp() {
    }

    @Test
    void getVisit_Returned() throws Exception {
        Visit visit = TestDataFactory.createVisit(LocalDateTime.of(2300, 10, 10, 10, 30), null, null);
        visitRepository.save(visit);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/visits/by-term/{term}", LocalDateTime.of(2300, 10, 10, 10, 30)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.term").value("2300-10-10T10:30:00"));
    }

    @Test
    void getVisits() throws Exception {
        List<Visit> visits = new ArrayList<>();
        visits.add(TestDataFactory.createVisit(LocalDateTime.of(2300, 10, 10, 10, 30), null, null));
        visitRepository.saveAll(visits);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/visits"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].term").value("2300-10-10T10:30:00"));
    }

    @Test
    void getPatientVisits_Returned() throws Exception {
        Patient patient = TestDataFactory.createPatient("test");
        List<Visit> visits = new ArrayList<>();
        visits.add(TestDataFactory.createVisit(LocalDateTime.of(2300, 10, 10, 10, 30), TestDataFactory.createPatient("test"), null));
        visits.add(TestDataFactory.createVisit(LocalDateTime.of(2100, 10, 10, 10, 30), TestDataFactory.createPatient("test"), null));
        visits.get(0).setId(null);
        visits.get(1).setId(null);
        patientRepository.save(patient);
        visitRepository.saveAll(visits);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/visits/{email}", "test"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].term").value("2300-10-10T10:30:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].term").value("2100-10-10T10:30:00"));

    }

    @Test
    void addPatientToVisit_Returned() throws Exception {
        Patient patient = TestDataFactory.createPatient("test");
        List<Visit> visits = new ArrayList<>();
        visits.add(TestDataFactory.createVisit(LocalDateTime.of(2300, 10, 10, 10, 30), null, null));
        visits.add(TestDataFactory.createVisit(LocalDateTime.of(2100, 10, 10, 10, 30), null, null));
        visits.get(0).setId(null);
        visits.get(1).setId(null);
        patient.setId(null);
        patientRepository.save(patient);
        visitRepository.saveAll(visits);
        String term = mapper.writeValueAsString(LocalDateTime.of(2300, 10, 10, 10, 30));
        this.mockMvc
                .perform(MockMvcRequestBuilders.patch("/visits/{email}", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(term))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.patient.id").value(1L));
    }

    @Test
    void addVisit_Returned() throws Exception {
        VisitCreationDto visitCreationDto = TestDataFactory.createVisitCreationDto(LocalDateTime.of(2300, 10, 10, 10, 30), null);
        String visitCreationDtoJson = mapper.writeValueAsString(visitCreationDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitCreationDtoJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.term").value("2300-10-10T10:30:00"));
    }

}
