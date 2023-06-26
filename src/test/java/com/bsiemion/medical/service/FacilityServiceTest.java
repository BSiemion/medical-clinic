package com.bsiemion.medical.service;

import com.bsiemion.medical.TestDataFactory;
import com.bsiemion.medical.mapper.DoctorMapper;
import com.bsiemion.medical.mapper.FacilityMapper;
import com.bsiemion.medical.model.dto.FacilityDto;
import com.bsiemion.medical.model.entity.Facility;
import com.bsiemion.medical.repository.FacilityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FacilityServiceTest {
    private FacilityRepository facilityRepository;
    private FacilityMapper facilityMapper;
    private DoctorMapper doctorMapper;
    private FacilityService facilityService;

    @BeforeEach
    void setUp(){
        this.facilityRepository = mock(FacilityRepository.class);
        this.facilityMapper = Mappers.getMapper(FacilityMapper.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.facilityService = new FacilityService(facilityRepository, facilityMapper, doctorMapper);
    }

    @Test
    void getAllFacilities_FacilitiesExist_Returned(){
        List<Facility> facilities = new ArrayList<>();
        facilities.add(TestDataFactory.createFacility("test"));
        facilities.add(TestDataFactory.createFacility("test1"));
        when(facilityRepository.findAll()).thenReturn(facilities);

        List<FacilityDto> result = facilityService.getAllFacilities();

        Assertions.assertEquals(2,result.size());
        Assertions.assertEquals("test", result.get(0).getName());
        Assertions.assertEquals("test1", result.get(1).getName());
    }
}
