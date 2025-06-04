package com.healthconnect.summary.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String allergenName;
    private String reaction;
    @NotBlank
    private String severity;
}
