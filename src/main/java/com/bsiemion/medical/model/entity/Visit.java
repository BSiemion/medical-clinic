package com.bsiemion.medical.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private LocalDateTime term;
    @ManyToOne
    private Patient patient;

    public Visit(LocalDateTime term) {
        this.term = term;
    }
    public boolean isAvailable(){
        return patient == null;
    }
}
