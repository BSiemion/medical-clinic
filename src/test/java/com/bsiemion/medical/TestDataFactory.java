package com.bsiemion.medical;

import com.bsiemion.medical.model.dto.DoctorCreationDto;
import com.bsiemion.medical.model.dto.PatientCreationDto;
import com.bsiemion.medical.model.dto.VisitCreationDto;
import com.bsiemion.medical.model.entity.Doctor;
import com.bsiemion.medical.model.entity.Facility;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.model.entity.Visit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public final class TestDataFactory {
    private TestDataFactory() {

    }

    public static Visit createVisit(LocalDateTime localDateTime, Patient patient, Long id) {
        return Visit.builder()
                .id(id)
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
    public static PatientCreationDto createPatientCreationDto(String email){
        return PatientCreationDto.builder()
                .email(email)
                .birthday(LocalDate.of(2000, 10, 10))
                .firstName("testName")
                .idCardNo("1352523523")
                .lastName("testLastName")
                .password("testPass")
                .phoneNumber("1235436")
                .build();
    }

    public static Doctor createDoctor(String email, Long id){
        return  Doctor.builder()
                .id(id)
                .email(email)
                .specialization("test specialization")
                .firstName("Andrzej")
                .lastName("Roztocki")
                .password("test password")
                .build();
    }

    public static DoctorCreationDto createDoctorCreationDto(String email){
        return  DoctorCreationDto.builder()
                .email(email)
                .specialization("test specialization")
                .firstName("Andrzej")
                .lastName("Roztocki")
                .password("test password")
                .build();
    }

    public static Facility createFacility(String name){
        return Facility.builder()
                .id(1L)
                .name(name)
                .city("Warszawa")
                .buildingNumber("13")
                .postalCode("16-300")
                .street("Ciasteczkowa")
                .doctors(new ArrayList<>())
                .build();
    }
}
