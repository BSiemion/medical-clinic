package com.bsiemion.medical.mapper;

import com.bsiemion.medical.model.dto.PatientDto;
import com.bsiemion.medical.model.dto.VisitCreationDto;
import com.bsiemion.medical.model.dto.VisitDto;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.model.entity.Visit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitDtoMapper {
    VisitDto visitToDto(Visit visit);
    Visit dtoToVisit(VisitCreationDto visitCreationDto);
}
