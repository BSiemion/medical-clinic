package com.bsiemion.medical;

import com.bsiemion.medical.model.dto.VisitCreationDto;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.model.entity.Visit;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestDataFactory {
    public static Visit createVisit(LocalDateTime localDateTime, Patient patient) {
        return Visit.builder()
                .id(1L)
                .term(localDateTime)
                .patient(patient)
                .build();
    }

    public static VisitCreationDto createVisitCreationDto(LocalDateTime localDateTime, Patient patient) {
        return VisitCreationDto.builder()
                .term(localDateTime)
                .patient(patient)
                .build();
    }

    public static Patient createPatient(String email) {
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
