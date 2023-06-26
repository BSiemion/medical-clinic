package com.bsiemion.medical.model.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityCreationDto {
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String buildingNumber;
}
