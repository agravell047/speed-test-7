package com.healthconnect.summary.service;

import com.healthconnect.summary.dto.PatientHealthSummaryResponse;

public interface PatientSummaryService {
    /**
     * Aggregates and synthesizes patient health data for the summary dashboard.
     * 
     * @param patientId the patient identifier
     * @return PatientHealthSummaryResponse containing the dashboard data
     */
    PatientHealthSummaryResponse getPatientHealthSummary(String patientId);
}
