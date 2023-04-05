package com.bsiemion.medical.servis;

import com.bsiemion.medical.exception.PatientIllegalDataException;
import com.bsiemion.medical.exception.PatientNotFoundException;
import com.bsiemion.medical.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private List<Patient> patients = new ArrayList<>();

    public String addPatient(Patient patient) {
        patients.add(patient);
        return "User added successfully";
    }

    public String removePatient(String email) {
        Patient patient = findPatient(email);
        patients.remove(patient);
        return "User removed successfully";
    }

    public String editPatient(String email, Patient editInfo) {
        Patient patient = findPatient(email);

        validatePatient(editInfo);
        if (!editInfo.getIdCardNo().equals(patient.getIdCardNo())) {
            throw new PatientIllegalDataException("You can't change your Id card number");
        }
        patient.editPatient(editInfo);
        return "User edited successfully";
    }

    private void validatePatient(Patient editInfo) {
        if (editInfo.getEmail() == null || editInfo.getPassword() == null || editInfo.getBirthday() == null ||
                editInfo.getLastName() == null || editInfo.getFirstName() == null || editInfo.getPhoneNumber() == null ||
                editInfo.getIdCardNo() == null) {
            throw new PatientIllegalDataException("Some value is null");
        }
    }

    public String editPassword(String email, String password) {
        Patient patient = findPatient(email);
        patient.setPassword(password);
        return "Password edited successfully";
    }

    public List<Patient> getAllPatients() {
        return patients;
    }

    public Patient getPatient(String email) {
        return findPatient(email);
    }

    private Patient findPatient(String email) {
        return patients.stream()
                .filter(patient1 -> patient1.getEmail().equals(email))
                .findFirst()
                .orElseThrow(PatientNotFoundException::new);
    }
}
