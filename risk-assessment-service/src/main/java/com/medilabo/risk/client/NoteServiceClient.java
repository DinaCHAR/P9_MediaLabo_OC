package com.medilabo.risk.client;

import com.medilabo.risk.dto.NoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "note-service", url = "${services.note.url}", path = "/api/notes")
public interface NoteServiceClient {


    @GetMapping("/patient/{patientId}")
    List<NoteDto> getNotesByPatientId(@PathVariable("patientId") Long patientId);
}