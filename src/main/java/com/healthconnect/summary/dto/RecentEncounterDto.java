package com.healthconnect.summary.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a recent encounter in the patient summary.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentEncounterDto {
    @NotBlank
    private String encounterId;
    @NotBlank
    private String date;
    @NotBlank
    private String reasonForVisit;
    private String primaryDiagnosis;
    private String primaryCareProvider;

    // New fields for per-visit aggregation
    private List<ActiveMedicationDto> medications;
    private List<LabResultDto> labResults;
    private List<UpcomingAppointmentDto> followUpAppointments;
    private SectionErrorDto medicationsError;
    private SectionErrorDto labsError;
    private SectionErrorDto followUpsError;
}
