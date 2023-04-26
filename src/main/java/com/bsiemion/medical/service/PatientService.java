package com.bsiemion.medical.service;

import com.bsiemion.medical.exception.PatientIllegalDataException;
import com.bsiemion.medical.exception.PatientNotFoundException;
import com.bsiemion.medical.mapper.PatientDtoMapper;
import com.bsiemion.medical.model.dto.MedicalMessageDto;
import com.bsiemion.medical.model.dto.PatientDto;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.repozitory.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientService {
    private PatientRepository patientRepository;
    private PatientDtoMapper patientDtoMapper;

    public PatientDto addPatient(Patient patient) {
        patientRepository.save(patient);
        return patientDtoMapper.patientToDto(patient);
    }

    public PatientDto removePatient(String email) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
        patientRepository.delete(patient);
        return patientDtoMapper.patientToDto(patient);
    }

    public PatientDto editPatient(String email, Patient editInfo) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);

        validatePatient(editInfo);
        if (!editInfo.getIdCardNo().equals(patient.getIdCardNo())) {
            throw new PatientIllegalDataException("You can't change your Id card number");
        }
        patient.editPatient(editInfo);
        patientRepository.save(patient);
        return patientDtoMapper.patientToDto(editInfo);
    }

    public MedicalMessageDto editPassword(String email, String password) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
        patient.setPassword(password);
        patientRepository.save(patient);
        return MedicalMessageDto.builder()
                .message("Password changed successfully")
                .build();
    }

    public List<PatientDto> getAllPatients() {
        List<Patient> allPatients = patientRepository.findAll();
        return allPatients.stream()
                .map(patient -> patientDtoMapper.patientToDto(patient))
                .collect(Collectors.toList());
    }

    public PatientDto getPatient(String email) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
        return patientDtoMapper.patientToDto(patient);
    }
    private void validatePatient(Patient editInfo) {
        if (editInfo.getEmail() == null || editInfo.getPassword() == null || editInfo.getBirthday() == null ||
                editInfo.getLastName() == null || editInfo.getFirstName() == null || editInfo.getPhoneNumber() == null ||
                editInfo.getIdCardNo() == null) {
            throw new PatientIllegalDataException("Some value is null");
        }
    }
}
