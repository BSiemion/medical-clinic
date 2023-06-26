package com.bsiemion.medical.controller;

import com.bsiemion.medical.model.dto.DoctorCreationDto;
import com.bsiemion.medical.model.dto.DoctorDto;
import com.bsiemion.medical.service.DoctorService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorDto> addDoctor(@RequestBody DoctorCreationDto doctorCreationDto) {
        return ResponseEntity.status(200).body(doctorService.addDoctor(doctorCreationDto));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<DoctorDto> removeDoctor(@PathVariable String email) {
        return ResponseEntity.status(200).body(doctorService.removeDoctor(email));
    }

    @PatchMapping("/{email}/assign/{facilityName}")
    public ResponseEntity<DoctorDto> addFacilityToDoctor(@PathVariable String email, @PathVariable String facilityName) {
        return ResponseEntity.status(200).body(doctorService.addFacilityToDoctor(email, facilityName));
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors(){
        return ResponseEntity.status(200).body(doctorService.getAllDoctors());
    }


}
