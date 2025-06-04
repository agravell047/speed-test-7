package com.healthconnect.summary.controller;

import com.healthconnect.summary.dto.PatientHealthSummaryResponse;
import com.healthconnect.summary.service.PatientSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the Patient Health Summary endpoint.
 */
@Slf4j
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Validated
@Tag(name = "Patient Health Summary", description = "Aggregated patient health summary dashboard API.")
public class SummaryController {

    private final PatientSummaryService patientSummaryService;

    @Operation(summary = "Get Patient Health Summary", description = "Returns a synthesized health summary for the given patient.", responses = {
            @ApiResponse(responseCode = "200", description = "Patient summary returned successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid patient ID supplied."),
            @ApiResponse(responseCode = "404", description = "Patient not found or no data available."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{patientId}/summary")
    public ResponseEntity<PatientHealthSummaryResponse> getPatientSummary(
            @PathVariable("patientId") @NotBlank String patientId) {
        log.info("Request received for patient summary: {}", patientId);
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary(patientId);
        return ResponseEntity.ok(response);
    }
}
