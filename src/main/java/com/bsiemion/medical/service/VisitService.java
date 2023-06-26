package com.bsiemion.medical.service;

import com.bsiemion.medical.exception.PatientNotFoundException;
import com.bsiemion.medical.exception.VisitIllegalDateException;
import com.bsiemion.medical.exception.VisitIsNotAvailableException;
import com.bsiemion.medical.exception.VisitNotFoundException;
import com.bsiemion.medical.mapper.VisitMapper;
import com.bsiemion.medical.model.dto.VisitCreationDto;
import com.bsiemion.medical.model.dto.VisitDto;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.model.entity.Visit;
import com.bsiemion.medical.repository.PatientRepository;
import com.bsiemion.medical.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final VisitMapper visitDtoMapper;

    public VisitDto addVisit(VisitCreationDto visitCreationDto) {
        LocalDateTime currentTime = LocalDateTime.now();
        Visit visit = visitDtoMapper.dtoToVisit(visitCreationDto);
        if (visit.getTerm().isBefore(currentTime) || visit.getTerm().isEqual(currentTime)) {
            throw new VisitIllegalDateException("You can not reserve visit before current time", HttpStatus.BAD_REQUEST);
        }
        if (visit.getTerm().getMinute() % 15 != 0) {
            throw new VisitIllegalDateException("You can only reserve every quarter", HttpStatus.BAD_REQUEST);
        }
        if (visitRepository.findByTerm(visit.getTerm()).isPresent()) {
            throw new VisitIsNotAvailableException("This date is already taken", HttpStatus.BAD_REQUEST);
        }
        visitRepository.save(visit);
        return visitDtoMapper.visitToDto(visit);
    }

    public VisitDto addPatientToVisit(String email, LocalDateTime term) {
        LocalDateTime currentTime = LocalDateTime.now();
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(()-> new VisitNotFoundException(HttpStatus.NOT_FOUND));
        Visit visit = visitRepository.findByTerm(term)
                .orElseThrow(()-> new VisitNotFoundException(HttpStatus.NOT_FOUND));
        if (visit.getTerm().isBefore(currentTime)) {
            throw new VisitIllegalDateException("You can not reserve visit before current time", HttpStatus.BAD_REQUEST);
        }
        if (!visit.isAvailable()) {
            throw new VisitIsNotAvailableException("This date is already taken", HttpStatus.BAD_REQUEST);
        }
        visit.setPatient(patient);
        visitRepository.save(visit);
        return visitDtoMapper.visitToDto(visit);
    }

    public List<VisitDto> getPatientVisits(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(()-> new PatientNotFoundException(HttpStatus.NOT_FOUND));
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
                .orElseThrow(()-> new VisitNotFoundException(HttpStatus.NOT_FOUND));
        return visitDtoMapper.visitToDto(visit);
    }
}
