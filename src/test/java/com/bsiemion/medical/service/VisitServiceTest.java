package com.bsiemion.medical.service;

import com.bsiemion.medical.TestDataFactory;
import com.bsiemion.medical.exception.PatientNotFoundException;
import com.bsiemion.medical.exception.VisitIllegalDateException;
import com.bsiemion.medical.exception.VisitIsNotAvailableException;
import com.bsiemion.medical.exception.VisitNotFoundException;
import com.bsiemion.medical.mapper.VisitDtoMapper;
import com.bsiemion.medical.model.dto.VisitCreationDto;
import com.bsiemion.medical.model.dto.VisitDto;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.model.entity.Visit;
import com.bsiemion.medical.repozitory.PatientRepository;
import com.bsiemion.medical.repozitory.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class VisitServiceTest {
    private VisitRepository visitRepository;
    private PatientRepository patientRepository;
    private VisitDtoMapper visitDtoMapper;
    private VisitService visitService;

    @BeforeEach
    void setUp() {
        this.visitRepository = mock(VisitRepository.class);
        this.visitDtoMapper = Mappers.getMapper(VisitDtoMapper.class);
        this.patientRepository = mock(PatientRepository.class);
        this.visitService = new VisitService(visitRepository, patientRepository, visitDtoMapper);
    }

    @Test
    void addVisit_NewVisit_Created() {
        VisitCreationDto visitCreationDto = TestDataFactory.createVisitCreationDto(LocalDateTime.of(2300, 10, 10, 10, 30), TestDataFactory.createPatient("test"));
        Visit visit = visitDtoMapper.dtoToVisit(visitCreationDto);
        when(visitRepository.findByTerm(eq(visit.getTerm()))).thenReturn(Optional.empty());
        when(visitRepository.save(eq(visit))).thenReturn(visit);

        VisitDto visitDto = visitService.addVisit(visitCreationDto);

        Assertions.assertEquals(LocalDateTime.of(2300, 10, 10, 10, 30), visitDto.getTerm());
        Assertions.assertNull(visitDto.getPatient());
        verify(visitRepository, times(1)).save(eq(visit));
        verify(visitRepository, times(1)).findByTerm(eq(visit.getTerm()));
    }

    @Test
    void addVisit_BeforeCurrentTime_ExceptionThrown() {
        VisitCreationDto visitCreationDto = TestDataFactory.createVisitCreationDto(LocalDateTime.of(2000, 10, 10, 10, 30), TestDataFactory.createPatient("test"));
        Visit visit = visitDtoMapper.dtoToVisit(visitCreationDto);
        when(visitRepository.findByTerm(eq(visit.getTerm()))).thenReturn(Optional.empty());

        VisitIllegalDateException result = Assertions.assertThrows(VisitIllegalDateException.class,
                () -> visitService.addVisit(visitCreationDto));
        Assertions.assertEquals("You can not reserve visit before current time", result.getMessage());
    }

    @Test
    void addVisit_NotEveryQuarter_ExceptionThrown() {
        VisitCreationDto visitCreationDto = TestDataFactory.createVisitCreationDto(LocalDateTime.of(2100, 10, 10, 10, 22), TestDataFactory.createPatient("test"));
        Visit visit = visitDtoMapper.dtoToVisit(visitCreationDto);
        when(visitRepository.findByTerm(eq(visit.getTerm()))).thenReturn(Optional.empty());

        VisitIllegalDateException result = Assertions.assertThrows(VisitIllegalDateException.class,
                () -> visitService.addVisit(visitCreationDto));
        Assertions.assertEquals("You can only reserve every quarter", result.getMessage());
    }

    @Test
    void addVisit_VisitNotAvailable_ExceptionThrown() {
        VisitCreationDto visitCreationDto = TestDataFactory.createVisitCreationDto(LocalDateTime.of(2100, 10, 10, 10, 30), TestDataFactory.createPatient("test"));
        Visit visit = visitDtoMapper.dtoToVisit(visitCreationDto);
        when(visitRepository.findByTerm(eq(visit.getTerm()))).thenReturn(Optional.of(new Visit()));

        VisitIsNotAvailableException result = Assertions.assertThrows(VisitIsNotAvailableException.class,
                () -> visitService.addVisit(visitCreationDto));
        Assertions.assertEquals("This date is already taken", result.getMessage());
    }

    @Test
    void addPatientToVisit_PatientAndVisitExist_Added() {
        Patient patient = TestDataFactory.createPatient("test");
        Visit visit = TestDataFactory.createVisit(LocalDateTime.of(2300, 10, 10, 10, 30), null);
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));
        when(visitRepository.findByTerm(eq(LocalDateTime.of(2300, 10, 10, 10, 30))))
                .thenReturn(Optional.of(visit));
        when(visitRepository.save(visit)).thenReturn(visit);

        VisitDto result = visitService.addPatientToVisit("test", LocalDateTime.of(2300, 10, 10, 10, 30));

        Assertions.assertEquals(patient, result.getPatient());
        verify(visitRepository, times(1)).findByTerm(LocalDateTime.of(2300, 10, 10, 10, 30));
        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    void addPatientToVisit_PatientDoesNotExist_ExceptionThrown() {
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.empty());

        PatientNotFoundException result = Assertions.assertThrows(PatientNotFoundException.class,
                () -> visitService.getPatientVisits("test"));
        Assertions.assertEquals("Patient not found", result.getMessage());
    }

//    @Test
//    void addPatientToVisit_VisitDoesNotExist_ExceptionThrown() {
//        Patient patient = createPatient("test");
//        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));
//        when(visitRepository.findByPatient(eq(patient))).thenReturn(Optional.empty());
//
//        VisitNotFoundException result = Assertions.assertThrows(VisitNotFoundException.class,
//                () -> visitService.getPatientVisits("test"));
//        Assertions.assertEquals("Visit not found", result.getMessage());
//    }

    @Test
    void addPatientToVisit_VisitBeforeCurrentTime_ExceptionThrown() {
        Patient patient = TestDataFactory.createPatient("test");
        Visit visit = TestDataFactory.createVisit(LocalDateTime.of(2000, 10, 10, 10, 30), null);
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));
        when(visitRepository.findByTerm(eq(LocalDateTime.of(2000, 10, 10, 10, 30))))
                .thenReturn(Optional.of(visit));

        VisitIllegalDateException result = Assertions.assertThrows(VisitIllegalDateException.class,
                () -> visitService.addPatientToVisit("test", LocalDateTime.of(2000, 10, 10, 10, 30)));
        Assertions.assertEquals("You can not reserve visit before current time", result.getMessage());
    }

    @Test
    void addPatientToVisit_VisitNotAvailable_ExceptionThrown() {
        Patient patient = TestDataFactory.createPatient("test");
        Visit visit = TestDataFactory.createVisit(LocalDateTime.of(2300, 10, 10, 10, 30), TestDataFactory.createPatient("123"));
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));
        when(visitRepository.findByTerm(eq(LocalDateTime.of(2300, 10, 10, 10, 30))))
                .thenReturn(Optional.of(visit));

        VisitIsNotAvailableException result = Assertions.assertThrows(VisitIsNotAvailableException.class,
                () -> visitService.addPatientToVisit("test", LocalDateTime.of(2300, 10, 10, 10, 30)));
        Assertions.assertEquals("This date is already taken", result.getMessage());
    }

//    @Test
//    void getPatientVisits_PatientExists_Returned() {
//        Patient patient = createPatient("test");
//        List<Visit> visits = new ArrayList<>();
//        visits.add(createVisit(LocalDateTime.of(2300, 10, 10, 10, 30), createPatient("test")));
//        visits.add(createVisit(LocalDateTime.of(2200, 5, 1, 10, 15), createPatient("test")));
//        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));
//        when(visitRepository.findByPatient(eq(patient))).thenReturn(Optional.of(visits));
//
//        List<VisitDto> result = visitService.getPatientVisits("test");
//        Assertions.assertEquals(2, result.size());
//        Assertions.assertEquals(patient, result.get(0).getPatient());
//        verify(visitRepository, times(1)).findByPatient(eq(patient));
//    }

    @Test
    void getPatientVisits_PatientDoesNotExist_ExceptionThrown() {
        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.empty());

        PatientNotFoundException result = Assertions.assertThrows(PatientNotFoundException.class,
                () -> visitService.getPatientVisits("test"));
        Assertions.assertEquals("Patient not found", result.getMessage());
    }

//    @Test
//    void getPatientVisits_VisitDoesNotExist_ExceptionThrown() {
//        Patient patient = createPatient("test");
//        when(patientRepository.findByEmail(eq("test"))).thenReturn(Optional.of(patient));
//        when(visitRepository.findByPatient(eq(patient))).thenReturn(Optional.empty());
//
//        VisitNotFoundException result = Assertions.assertThrows(VisitNotFoundException.class,
//                () -> visitService.getPatientVisits("test"));
//        Assertions.assertEquals("Visit not found", result.getMessage());
//    }

    @Test
    void getVisits_VisitExists_Returned() {
        List<Visit> visits = new ArrayList<>();
        visits.add(TestDataFactory.createVisit(LocalDateTime.of(2300, 10, 10, 10, 30), TestDataFactory.createPatient("test")));
        visits.add(TestDataFactory.createVisit(LocalDateTime.of(2200, 5, 1, 10, 15), TestDataFactory.createPatient("test")));
        when(visitRepository.findAll()).thenReturn(visits);

        List<VisitDto> result = visitService.getVisits();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(LocalDateTime.of(2200, 5, 1, 10, 15), result.get(1).getTerm());
    }

    @Test
    void getVisit_VisitExists_Returned() {
        Visit visit = TestDataFactory.createVisit(LocalDateTime.of(2300, 10, 10, 10, 30), TestDataFactory.createPatient("test"));
        when(visitRepository.findByTerm(eq(LocalDateTime.of(2300, 10, 10, 10, 30))))
                .thenReturn(Optional.of(visit));

        VisitDto result = visitService.getVisit(LocalDateTime.of(2300, 10, 10, 10, 30));
        Assertions.assertEquals(LocalDateTime.of(2300, 10, 10, 10, 30), result.getTerm());
    }

    @Test
    void getVisit_VisitDoesNotExist_ExceptionThrown() {
        when(visitRepository.findByTerm(LocalDateTime.of(2300, 10, 10, 10, 30)))
                .thenReturn(Optional.empty());

        VisitNotFoundException result = Assertions.assertThrows(VisitNotFoundException.class,
                () -> visitService.getVisit(LocalDateTime.of(2300, 10, 10, 10, 30)));
        Assertions.assertEquals("Visit not found", result.getMessage());
    }

}
