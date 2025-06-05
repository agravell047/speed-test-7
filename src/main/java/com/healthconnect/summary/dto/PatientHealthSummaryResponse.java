package com.healthconnect.summary.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Valid
    private List<RecentEncounterDto> recentEncounters;
    @NotNull
    @Valid
    private List<ActiveMedicationDto> activeMedications;
    @NotNull
    @Valid
    private List<CriticalAllergyDto> criticalAllergies;
    @NotNull
    @Valid
    private List<UpcomingAppointmentDto> upcomingAppointments;
    @NotNull
    @Valid
    private List<ErrorDto> errors;

    // Section-specific error fields
    private SectionErrorDto appointmentsError;
    private SectionErrorDto encountersError;
    private SectionErrorDto allergiesError;
    private SectionErrorDto medicationsError;
    private SectionErrorDto labsError;
}
