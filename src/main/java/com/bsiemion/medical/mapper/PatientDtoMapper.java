package com.bsiemion.medical.mapper;

import com.bsiemion.medical.model.dto.PatientDto;
import com.bsiemion.medical.model.entity.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientDtoMapper {
    PatientDto patientToDto(Patient patient);
    Patient dtoToPatient(PatientDto patientDto);
}
