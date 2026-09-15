package com.medilabo.patient.mapper;

import com.medilabo.patient.dto.CreatePatientRequest;
import com.medilabo.patient.dto.PatientDto;
import com.medilabo.patient.dto.UpdatePatientRequest;
import com.medilabo.patient.model.Patient;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto toDto(Patient patient);

    List<PatientDto> toDtoList(List<Patient> patients);

    @Mapping(target = "id", ignore = true)
    Patient toEntity(CreatePatientRequest createRequest);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(UpdatePatientRequest updateRequest, @MappingTarget Patient patient);
}