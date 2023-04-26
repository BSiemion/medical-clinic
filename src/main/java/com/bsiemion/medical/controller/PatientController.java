package com.bsiemion.medical.controller;

import com.bsiemion.medical.model.dto.MedicalMessageDto;
import com.bsiemion.medical.model.dto.PatientDto;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/patients")
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientDto> addPatient(@RequestBody Patient patient){
        return ResponseEntity.status(200).body(patientService.addPatient(patient));
    }
    @DeleteMapping("/{email}")
    public ResponseEntity<PatientDto> removePatient(@PathVariable String email){
        return ResponseEntity.status(200).body(patientService.removePatient(email));
    }
    @PutMapping("/{email}")
    public ResponseEntity<PatientDto> editPatient(@PathVariable String email, @RequestBody Patient editInfo){
        return ResponseEntity.status(200).body(patientService.editPatient(email, editInfo));
    }
    @PatchMapping("/{email}")
    public ResponseEntity<MedicalMessageDto> editPassword(@PathVariable String email, @RequestBody String password){
        return ResponseEntity.status(200).body(patientService.editPassword(email, password));
    }
    @GetMapping("/{email}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable String email) {
        return ResponseEntity.status(200).body(patientService.getPatient(email));
    }
    @GetMapping
    public List<PatientDto> getPatients(){
        return patientService.getAllPatients();
    }
}
