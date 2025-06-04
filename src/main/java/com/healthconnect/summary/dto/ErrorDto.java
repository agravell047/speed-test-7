package com.healthconnect.summary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for error objects in the patient summary response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    @NotBlank
    private String code;
    @NotBlank
    private String message;
}
