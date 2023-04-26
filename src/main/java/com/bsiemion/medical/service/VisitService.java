package com.bsiemion.medical.service;

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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final VisitDtoMapper visitDtoMapper;

    public VisitDto addVisit(VisitCreationDto visitCreationDto) {
        LocalDateTime currentTime = LocalDateTime.now();
        Visit visit = visitDtoMapper.dtoToVisit(visitCreationDto);
        if (visit.getTerm().isBefore(currentTime) || visit.getTerm().isEqual(currentTime)) {
            throw new VisitIllegalDateException("You can not reserve visit before current time");
        }
        if (visit.getTerm().getMinute() % 15 != 0) {
            throw new VisitIllegalDateException("You can only reserve every quarter");
        }
        if (visitRepository.findByTerm(visit.getTerm()).isPresent()) {
            throw new VisitIsNotAvailableException("This date is already taken");
        }
        visitRepository.save(visit);
        return visitDtoMapper.visitToDto(visit);
    }

    public VisitDto addPatientToVisit(String email, LocalDateTime term) {
        LocalDateTime currentTime = LocalDateTime.now();
        Patient patient = patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
        Visit visit = visitRepository.findByTerm(term).orElseThrow(VisitNotFoundException::new);
        if (visit.getTerm().isBefore(currentTime)) {
            throw new VisitIllegalDateException("You can not reserve visit before current time");
        }
        if (!visit.isAvailable()) {
            throw new VisitIsNotAvailableException("This date is already taken");
        }
        visit.setPatient(patient);
        visitRepository.save(visit);
        return visitDtoMapper.visitToDto(visit);
    }

    public List<VisitDto> getPatientVisits(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);
        List<Visit> visits = visitRepository.findByPatient(patient);

        return visits.stream()
                .map(visit -> visitDtoMapper.visitToDto(visit))
                .collect(Collectors.toList());
    }

    public List<VisitDto> getVisits() {
        List<Visit> allVisits = visitRepository.findAll();
        return allVisits.stream()
                .map(visit -> visitDtoMapper.visitToDto(visit))
                .collect(Collectors.toList());
    }

    public VisitDto getVisit(LocalDateTime term) {
        Visit visit = visitRepository.findByTerm(term)
                .orElseThrow(VisitNotFoundException::new);
        return visitDtoMapper.visitToDto(visit);
    }

////    private static LocalDateTime getLocalDateTime() {
////        LocalDateTime actualTime = LocalDateTime.now();
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////        String formattedActualTime = actualTime.format(formatter);
////        return LocalDateTime.parse(formattedActualTime, formatter);
////    }
}
