package com.bsiemion.medical.repository;

import com.bsiemion.medical.model.entity.Doctor;
import com.bsiemion.medical.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
}
