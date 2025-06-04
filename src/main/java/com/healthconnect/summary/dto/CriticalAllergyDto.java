package com.healthconnect.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a critical allergy in the patient summary.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriticalAllergyDto {
    private String allergenName;
    private String reaction;
    private String severity;
}
