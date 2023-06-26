package com.bsiemion.medical.service;

import com.bsiemion.medical.exception.PatientIllegalDataException;
import com.bsiemion.medical.exception.PatientNotFoundException;
import com.bsiemion.medical.mapper.PatientMapper;
import com.bsiemion.medical.model.dto.MedicalMessageDto;
import com.bsiemion.medical.model.dto.PatientCreationDto;
import com.bsiemion.medical.model.dto.PatientDto;
import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientDto addPatient(PatientCreationDto patientCreationDto) {
        Patient patient = patientMapper.dtoToPatient(patientCreationDto);
        patientRepository.save(patient);
        return patientMapper.patientToDto(patient);
    }

    public PatientDto removePatient(String email) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(()->new PatientNotFoundException(HttpStatus.NOT_FOUND));
        patientRepository.delete(patient);
        return patientMapper.patientToDto(patient);
    }

    public PatientDto editPatient(String email, Patient editInfo) {
        validatePatient(editInfo);
        Patient patient = patientRepository.findByEmail(email).orElseThrow(()->new PatientNotFoundException(HttpStatus.NOT_FOUND));

        if (!editInfo.getIdCardNo().equals(patient.getIdCardNo())) {
            throw new PatientIllegalDataException("You can't change your Id card number", HttpStatus.BAD_REQUEST);
        }
        patient.editPatient(editInfo);
        patientRepository.save(patient);
        return patientMapper.patientToDto(editInfo);
    }

    public MedicalMessageDto editPassword(String email, String password) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(()->new PatientNotFoundException(HttpStatus.NOT_FOUND));
        patient.setPassword(password);
        patientRepository.save(patient);
        return MedicalMessageDto.builder()
                .message("Password changed successfully")
                .build();
    }

    public List<PatientDto> getAllPatients() {
        List<Patient> allPatients = patientRepository.findAll();
        return allPatients.stream()
                .map(patientMapper::patientToDto)
                .collect(Collectors.toList());
    }

    public PatientDto getPatient(String email) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(()->new PatientNotFoundException(HttpStatus.NOT_FOUND));
        return patientMapper.patientToDto(patient);
    }
    private void validatePatient(Patient editInfo) {
        if (editInfo.getEmail() == null || editInfo.getPassword() == null || editInfo.getBirthday() == null ||
                editInfo.getLastName() == null || editInfo.getFirstName() == null || editInfo.getPhoneNumber() == null ||
                editInfo.getIdCardNo() == null) {
            throw new PatientIllegalDataException("values:email, password, birthday, last name, name, phone number, id card number are required", HttpStatus.BAD_REQUEST);
        }
    }
}
