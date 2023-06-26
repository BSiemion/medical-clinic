package com.bsiemion.medical.service;

import com.bsiemion.medical.exception.FacilityNotFoundException;
import com.bsiemion.medical.mapper.DoctorMapper;
import com.bsiemion.medical.mapper.FacilityMapper;
import com.bsiemion.medical.model.dto.DoctorDto;
import com.bsiemion.medical.model.dto.FacilityCreationDto;
import com.bsiemion.medical.model.dto.FacilityDto;
import com.bsiemion.medical.model.entity.Facility;
import com.bsiemion.medical.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityDtoMapper;
    private final DoctorMapper doctorDtoMapper;

    public List<FacilityDto> getAllFacilities() {
        List<Facility> allFacilities = facilityRepository.findAll();
        return allFacilities.stream()
                .map(facilityDtoMapper::facilityToDto)
                .collect(Collectors.toList());
    }

    public List<DoctorDto> getAllDoctorsFromFacility(String facilityName) {
        Facility facility = facilityRepository.findByName(facilityName)
                .orElseThrow(() -> new FacilityNotFoundException(HttpStatus.NOT_FOUND));
        return facility.getDoctors().stream()
                .map(doctorDtoMapper::doctorToDto)
                .collect(Collectors.toList());
    }

    public FacilityDto addFacility(FacilityCreationDto facilityCreationDto) {
        Facility facility = facilityDtoMapper.dtoToFacility(facilityCreationDto);
        facilityRepository.save(facility);
        return facilityDtoMapper.facilityToDto(facility);
    }
}
