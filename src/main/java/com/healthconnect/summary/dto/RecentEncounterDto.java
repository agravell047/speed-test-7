package com.healthconnect.summary.dto;

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
    private String encounterId;
    private String date;
    private String reasonForVisit;
    private String primaryDiagnosis;
    private String primaryCareProvider;
}
