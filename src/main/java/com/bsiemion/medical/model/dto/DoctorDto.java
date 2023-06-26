package com.bsiemion.medical.model.dto;

import com.bsiemion.medical.model.entity.Facility;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDto {
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
    private List<String> facilityIds;
}
