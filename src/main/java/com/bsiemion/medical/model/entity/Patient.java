package com.bsiemion.medical.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
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
