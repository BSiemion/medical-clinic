package com.bsiemion.medical.mapper;

import com.bsiemion.medical.model.dto.FacilityCreationDto;
import com.bsiemion.medical.model.dto.FacilityDto;
import com.bsiemion.medical.model.entity.Facility;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacilityMapper {
    FacilityDto facilityToDto(Facility facility);
    Facility dtoToFacility(FacilityCreationDto facilityCreationDto);
}
