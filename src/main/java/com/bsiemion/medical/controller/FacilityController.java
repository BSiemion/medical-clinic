package com.bsiemion.medical.controller;

import com.bsiemion.medical.model.dto.DoctorDto;
import com.bsiemion.medical.model.dto.FacilityCreationDto;
import com.bsiemion.medical.model.dto.FacilityDto;
import com.bsiemion.medical.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/facilities")
public class FacilityController {
    private final FacilityService facilityService;
    @PostMapping
    public ResponseEntity<FacilityDto> addFacility(@RequestBody FacilityCreationDto facilityCreationDto){
        return ResponseEntity.status(200).body(facilityService.addFacility(facilityCreationDto));
    }
    @GetMapping
    public ResponseEntity<List<FacilityDto>> getAllFacilities(){
        return ResponseEntity.status(200).body(facilityService.getAllFacilities());
    }

    @GetMapping("/{facilityName}")
    public ResponseEntity<List<DoctorDto>> getAllDoctorsFromFacility(@PathVariable String facilityName){
        return ResponseEntity.status(200).body(facilityService.getAllDoctorsFromFacility(facilityName));
    }

}
