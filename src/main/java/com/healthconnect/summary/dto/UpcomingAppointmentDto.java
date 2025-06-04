package com.healthconnect.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for an upcoming appointment in the patient summary.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingAppointmentDto {
    private String appointmentId;
    private String dateTime;
    private String type;
    private String provider;
    private String location;
}
