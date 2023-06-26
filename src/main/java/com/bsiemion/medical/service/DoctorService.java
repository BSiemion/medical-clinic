package com.bsiemion.medical.service;

import com.bsiemion.medical.exception.DoctorNotFoundException;
import com.bsiemion.medical.exception.FacilityNotFoundException;
import com.bsiemion.medical.mapper.DoctorMapper;
import com.bsiemion.medical.model.dto.DoctorCreationDto;
import com.bsiemion.medical.model.dto.DoctorDto;
import com.bsiemion.medical.model.entity.Doctor;
import com.bsiemion.medical.model.entity.Facility;
import com.bsiemion.medical.repository.DoctorRepository;
import com.bsiemion.medical.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;
    private final DoctorMapper doctorDtoMapper;

    public DoctorDto addDoctor(DoctorCreationDto doctorCreationDto) {
        Doctor doctor = doctorDtoMapper.dtoToDoctor(doctorCreationDto);
        doctorRepository.save(doctor);
        return doctorDtoMapper.doctorToDto(doctor);
    }

    @Transactional
    public DoctorDto removeDoctor(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException(HttpStatus.NOT_FOUND));
        doctorRepository.delete(doctor);
        return doctorDtoMapper.doctorToDto(doctor);
    }

    public DoctorDto addFacilityToDoctor(String email, String facilityName) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException(HttpStatus.NOT_FOUND));
        Facility facility = facilityRepository.findByName(facilityName)
                .orElseThrow(() -> new FacilityNotFoundException(HttpStatus.NOT_FOUND));
        doctor.getFacilities().add(facility);
        doctorRepository.save(doctor);
        return doctorDtoMapper.doctorToDto(doctor);
    }


    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorDtoMapper::doctorToDto)
                .collect(Collectors.toList());
    }
}
