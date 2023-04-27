package com.bsiemion.medical.repository;

import com.bsiemion.medical.model.entity.Patient;
import com.bsiemion.medical.model.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    Optional<Visit> findByTerm(LocalDateTime term);
    List<Visit> findByPatient(Patient patient);
}
