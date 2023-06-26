package com.bsiemion.medical.mapper;

import com.bsiemion.medical.model.dto.DoctorCreationDto;
import com.bsiemion.medical.model.dto.DoctorDto;
import com.bsiemion.medical.model.entity.Doctor;
import com.bsiemion.medical.model.entity.Facility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(source = "facilities", target = "facilityIds", qualifiedByName = "mapFacilities")
    DoctorDto doctorToDto(Doctor doctor);

    Doctor dtoToDoctor(DoctorCreationDto doctorCreationDto);

    @Named("mapFacilities")
    default List<String> mapFacilities(List<Facility> facilities) {
        if(facilities == null){
            return Collections.emptyList();
        }
        return facilities.stream()
                .map(facility -> facility.getId().toString())
                .collect(Collectors.toList());
    }
}
