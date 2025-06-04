package com.healthconnect.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for an active medication in the patient summary.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveMedicationDto {
    private String medicationName;
    private String dosage;
    private String route;
    private String startDate;
}
