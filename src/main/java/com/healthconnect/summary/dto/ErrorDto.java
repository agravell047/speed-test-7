package com.healthconnect.summary.dto;

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
    private String code;
    private String message;
}
