package com.bsiemion.medical.model.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorCreationDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String specialization;
}
