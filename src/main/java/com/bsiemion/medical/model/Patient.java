package com.bsiemion.medical.model;

import lombok.*;

import java.time.LocalDate;
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Patient {
    private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;

    public void editPatient(Patient patient){
        this.setBirthday(patient.getBirthday());
        this.setEmail(patient.getEmail());
        this.setPassword(patient.getPassword());
        this.setFirstName(patient.getFirstName());
        this.setLastName(patient.getLastName());
        this.setIdCardNo(patient.getIdCardNo());
        this.setPhoneNumber(patient.getPhoneNumber());
    }
}
