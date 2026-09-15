package com.medilabo.patient.repository;

import com.medilabo.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {


    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            @Param("firstName") String firstName, @Param("lastName") String lastName);
}