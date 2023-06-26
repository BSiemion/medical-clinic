package com.bsiemion.medical.model.dto;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityDto {
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String buildingNumber;
}
