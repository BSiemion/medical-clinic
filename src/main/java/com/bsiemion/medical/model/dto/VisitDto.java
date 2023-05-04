package com.bsiemion.medical.model.dto;

import com.bsiemion.medical.model.entity.Patient;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitDto {
    private Long id;
    private LocalDateTime term;
    private Patient patient;
}
