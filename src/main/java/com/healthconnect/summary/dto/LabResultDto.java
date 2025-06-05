package com.healthconnect.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a lab result in the patient summary.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabResultDto {
    private String observationId;
    private String testName;
    private String value;
    private String unit;
    private String status;
    private String effectiveDateTime;
}
