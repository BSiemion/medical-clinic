package com.bsiemion.medical.controller;

import com.bsiemion.medical.model.dto.VisitCreationDto;
import com.bsiemion.medical.model.dto.VisitDto;
import com.bsiemion.medical.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(path = "/visits")
@RestController
public class VisitController {
    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<VisitDto> addVisit(@RequestBody VisitCreationDto visitCreationDto) {
        return ResponseEntity.status(200).body(visitService.addVisit(visitCreationDto));
    }

    @PatchMapping("/{email}")
    public ResponseEntity<VisitDto> addPatientToVisit(@PathVariable String email, @RequestBody LocalDateTime term) {
        return ResponseEntity.status(200).body(visitService.addPatientToVisit(email, term));
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<VisitDto>> getPatientVisits(@PathVariable String email) {
        return ResponseEntity.status(200).body(visitService.getPatientVisits(email));
    }

    @GetMapping
    public ResponseEntity<List<VisitDto>> getVisits() {
        return ResponseEntity.status(200).body(visitService.getVisits());
    }

    @GetMapping("/by-term/{term}")
    public ResponseEntity<VisitDto> getVisit(@PathVariable LocalDateTime term) {
        return ResponseEntity.status(200).body(visitService.getVisit(term));
    }

}
