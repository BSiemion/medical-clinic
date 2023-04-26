package com.bsiemion.medical.model.dto;

import com.bsiemion.medical.model.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@Builder
public class VisitDto {
    private Long id;
    private LocalDateTime term;
    private Patient patient;
}
