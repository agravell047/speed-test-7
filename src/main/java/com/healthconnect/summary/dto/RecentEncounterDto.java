package com.healthconnect.summary.dto;

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
}
