package com.medilabo.risk.client;

import com.medilabo.risk.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "patient-service", url = "${services.patient.url}", path = "/api/patients")
public interface PatientServiceClient {


    @GetMapping("/{id}")
    PatientDto getPatientById(@PathVariable("id") Long id);
}