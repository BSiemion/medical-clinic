package com.bsiemion.medical.service;

import com.bsiemion.medical.TestDataFactory;
import com.bsiemion.medical.exception.PatientIllegalDataException;
import com.bsiemion.medical.exception.PatientNotFoundException;
import com.bsiemion.medical.mapper.PatientMapper;
import com.bsiemion.medical.model.dto.PatientCreationDto;
import com.bsiemion.medical.model.dto.PatientDto;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    private PatientRepository patientRepository;
    private PatientMapper mapper;
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        this.patientRepository = mock(PatientRepository.class);
        this.mapper = Mappers.getMapper(PatientMapper.class);
        this.patientService = new PatientService(patientRepository, mapper);
    }

    @Test
    void addPatient_NewPatient_Created() {
        PatientCreationDto patientCreationDto = TestDataFactory.createPatientCreationDto("test");
        Patient patient = mapper.dtoToPatient(patientCreationDto);
        when(patientRepository.save(eq(patient))).thenReturn(patient);

        PatientDto patientDto = patientService.addPatient(patientCreationDto);

        Assertions.assertEquals("test", patientDto.getEmail());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void removePatient_PatientExists_Removed() {
        Patient patient = TestDataFactory.createPatient("test");
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));

        PatientDto result = patientService.removePatient("test");

        Assertions.assertEquals("test", result.getEmail());
        verify(patientRepository, times(1)).delete(eq(patient));
        verify(patientRepository, times(1)).findByEmail(eq("test"));
    }

    @Test
    void removePatient_PatientDoesNotExist_ExceptionThrown() {
        when(patientRepository.findByEmail(eq("123"))).thenReturn(Optional.empty());

        PatientNotFoundException result = Assertions.assertThrows(PatientNotFoundException.class,
                () -> patientService.removePatient("123"));

        Assertions.assertEquals("Patient not found", result.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "liame", "pifpaf"})
    void editPatient_PatientExists_Edited(String email) {
        Patient editInfo = Patient.builder()
                .email(email)
                .id(1L)
                .birthday(LocalDate.of(2000, 10, 10))
                .firstName("testName")
                .lastName("razdwa")
                .idCardNo("1352523523")
                .password("testPass")
                .phoneNumber("1235436")
                .build();
        Patient patient = TestDataFactory.createPatient("test");
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));
        when(patientRepository.save(eq(patient))).thenReturn(patient);

        PatientDto result = patientService.editPatient("test", editInfo);

        Assertions.assertEquals(email, result.getEmail());
        Assertions.assertEquals("razdwa", result.getLastName());

        verify(patientRepository, times(1)).save(patient);
        verify(patientRepository, times(1)).findByEmail("test");
    }

    @ParameterizedTest
    @NullSource
    void editPatient_ValidationFailed_ExceptionThrown(String phoneNumber) {
        Patient editInfo = Patient.builder()
                .email("Andrzej")
                .id(1L)
                .birthday(LocalDate.of(2000, 10, 10))
                .firstName("testName")
                .lastName("razdwa")
                .idCardNo("1352523523")
                .password("testPass")
                .phoneNumber(phoneNumber)
                .build();
        Patient patient = TestDataFactory.createPatient("test");
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));

        PatientIllegalDataException result = Assertions.assertThrows(PatientIllegalDataException.class,
                () -> patientService.editPatient("test", editInfo));
        Assertions.assertEquals("values:email, password, birthday, last name, name, phone number, id card number are required"
                , result.getMessage());
    }

    @Test
    void editPatient_IdCardNumberEdited_ExceptionThrown() {
        Patient editInfo = Patient.builder()
                .email("Andrzej")
                .id(1L)
                .birthday(LocalDate.of(2000, 10, 10))
                .firstName("testName")
                .lastName("razdwa")
                .idCardNo("1")
                .password("testPass")
                .phoneNumber("23526")
                .build();
        Patient patient = TestDataFactory.createPatient("test");
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));

        PatientIllegalDataException result = Assertions.assertThrows(PatientIllegalDataException.class,
                () -> patientService.editPatient("test", editInfo));
        Assertions.assertEquals("You can't change your Id card number", result.getMessage());
    }

    @ParameterizedTest
    @MethodSource
    void editPassword_PatientExist_MessageReturned(String email, String password) {
        Patient patient = TestDataFactory.createPatient(email);
        when(patientRepository.findByEmail(eq(email))).thenReturn(Optional.of(patient));
        when(patientRepository.save(eq(patient))).thenReturn(patient);

        patientService.editPassword(email, password);

        Assertions.assertEquals(password, patient.getPassword());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void editPassword_PatientDoesNotExist_ExceptionThrown() {
        when(patientRepository.findByEmail(eq("123"))).thenReturn(Optional.empty());

        PatientNotFoundException result = Assertions.assertThrows(PatientNotFoundException.class,
                () -> patientService.editPassword("test", "123"));
        Assertions.assertEquals("Patient not found", result.getMessage());
    }

    @Test
    void getAllPatients_PatientsExist_Returned() {
        List<Patient> patients = new ArrayList<>();
        patients.add(TestDataFactory.createPatient("test"));
        patients.add(TestDataFactory.createPatient("test1"));
        when(patientRepository.findAll()).thenReturn(patients);

        List<PatientDto> result = patientService.getAllPatients();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("test", result.get(0).getEmail());
        Assertions.assertEquals("test1", result.get(1).getEmail());
    }

    @Test
    void getPatient_WithCorrectEmail_Returned() {
        Patient patient = TestDataFactory.createPatient("test");
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));

        PatientDto result = patientService.getPatient("test");

        Assertions.assertEquals("test", result.getEmail());
        verify(patientRepository, times(1)).findByEmail(eq("test"));
    }

    @Test
    void getPatient_PatientDoesNotExist_ExceptionThrown() {
        when(patientRepository.findByEmail(eq("123"))).thenReturn(Optional.empty());

        PatientNotFoundException result = Assertions.assertThrows(PatientNotFoundException.class,
                () -> patientService.getPatient("123"));
        Assertions.assertEquals("Patient not found", result.getMessage());
    }

    private static Stream<Arguments> editPassword_PatientExist_MessageReturned() {
        return Stream.of(
                Arguments.of("test", "haslo"),
                Arguments.of("email", "olsah"),
                Arguments.of("razdwa", "lohas")
        );
    }
}