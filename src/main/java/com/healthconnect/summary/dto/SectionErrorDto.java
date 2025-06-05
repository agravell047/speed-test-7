package com.healthconnect.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for section-specific error reporting in the patient summary response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionErrorDto {
    private String code;
    private String message;
}
