package com.bsiemion.medical.service;

import com.bsiemion.medical.TestDataFactory;
import com.bsiemion.medical.exception.DoctorNotFoundException;
import com.bsiemion.medical.exception.FacilityNotFoundException;
import com.bsiemion.medical.mapper.DoctorMapper;
import com.bsiemion.medical.model.dto.DoctorCreationDto;
import com.bsiemion.medical.model.dto.DoctorDto;
import com.bsiemion.medical.model.entity.Doctor;
import com.bsiemion.medical.model.entity.Facility;
import com.bsiemion.medical.repository.DoctorRepository;
import com.bsiemion.medical.repository.FacilityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {
    private DoctorRepository doctorRepository;
    private FacilityRepository facilityRepository;
    private DoctorMapper doctorMapper;
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        this.doctorRepository = mock(DoctorRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.facilityRepository = mock(FacilityRepository.class);
        this.doctorService = new DoctorService(doctorRepository, facilityRepository, doctorMapper);
    }

    @Test
    void addDoctor_NewDoctor_Created() {
        DoctorCreationDto doctorCreationDto = TestDataFactory.createDoctorCreationDto("test");
        Doctor doctor = doctorMapper.dtoToDoctor(doctorCreationDto);
        when(doctorRepository.save(eq(doctor))).thenReturn(doctor);

        DoctorDto result = doctorService.addDoctor(doctorCreationDto);

        Assertions.assertEquals("test", result.getEmail());
        verify(doctorRepository, times(1)).save(eq(doctor));
    }

    @Test
    void removeDoctor_DoctorExists_Removed() {
        Doctor doctor = TestDataFactory.createDoctor("test", 1L);
        when(doctorRepository.findByEmail(eq("test"))).thenReturn(Optional.of(doctor));

        DoctorDto result = doctorService.removeDoctor("test");

        Assertions.assertEquals("test", result.getEmail());
        verify(doctorRepository, times(1)).delete(eq(doctor));
        verify(doctorRepository, times(1)).findByEmail(eq("test"));
    }

    @Test
    void removeDoctor_DoctorDoesNotExist_ExceptionThrown() {
        when(doctorRepository.findByEmail(eq("123"))).thenReturn(Optional.empty());

        DoctorNotFoundException result = Assertions.assertThrows(DoctorNotFoundException.class,
                () -> doctorService.removeDoctor("123"));

        Assertions.assertEquals("Doctor not found", result.getMessage());
    }

    @Test
    void addFacilityToDoctor_DoctorAndFacilityExist_Added() {
        Doctor doctor = TestDataFactory.createDoctor("test", 1L);
        doctor.setFacilities(new LinkedList<>());
        Facility facility = TestDataFactory.createFacility("test");
        when(doctorRepository.findByEmail(eq("test"))).thenReturn(Optional.of(doctor));
        when(facilityRepository.findByName(eq("test"))).thenReturn(Optional.of(facility));
        when(doctorRepository.save(eq(doctor))).thenReturn(doctor);

        DoctorDto result = doctorService.addFacilityToDoctor("test", "test");

        Assertions.assertEquals(facility, result.getFacilityIds().get(0));
        verify(doctorRepository, times(1)).findByEmail("test");
        verify(facilityRepository, times(1)).findByName("test");
        verify(doctorRepository, times(1)).save(doctor);
    }

    @Test
    void addFacilityToDoctor_DoctorDoesNotExist_ExceptionThrown() {
        when(doctorRepository.findByEmail(eq("test"))).thenReturn(Optional.empty());

        DoctorNotFoundException result = Assertions.assertThrows(DoctorNotFoundException.class,
                () -> doctorService.addFacilityToDoctor("test", "test"));

        Assertions.assertEquals("Doctor not found", result.getMessage());
    }

    @Test
    void addFacilityToDoctor_FacilityDoesNotExist_ExceptionThrown() {
        Doctor doctor = TestDataFactory.createDoctor("test", 1L);
        when(doctorRepository.findByEmail(eq("test"))).thenReturn(Optional.of(doctor));
        when(facilityRepository.findByName(eq("test"))).thenReturn(Optional.empty());

        FacilityNotFoundException result = Assertions.assertThrows(FacilityNotFoundException.class,
                () -> doctorService.addFacilityToDoctor("test", "test"));

        Assertions.assertEquals("Facility not found", result.getMessage());
    }

    @Test
    void getAllDoctors_DoctorsExist_Returned() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(TestDataFactory.createDoctor("test", 1L));
        doctors.add(TestDataFactory.createDoctor("test1", 2L));
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<DoctorDto> result = doctorService.getAllDoctors();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("test", result.get(0).getEmail());
        Assertions.assertEquals("test1", result.get(1).getEmail());
    }


}
