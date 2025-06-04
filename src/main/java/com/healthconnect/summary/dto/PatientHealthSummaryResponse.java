package com.healthconnect.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for the patient health summary response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientHealthSummaryResponse {
    private List<RecentEncounterDto> recentEncounters;
    private List<ActiveMedicationDto> activeMedications;
    private List<CriticalAllergyDto> criticalAllergies;
    private List<UpcomingAppointmentDto> upcomingAppointments;
    private List<ErrorDto> errors;
}
