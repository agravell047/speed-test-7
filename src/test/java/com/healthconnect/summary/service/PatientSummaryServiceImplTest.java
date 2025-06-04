package com.healthconnect.summary.service;

import com.healthconnect.summary.dto.PatientHealthSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientSummaryServiceImplTest {

    @Mock
    private FhirApiClient fhirApiClient;

    @InjectMocks
    private PatientSummaryServiceImpl patientSummaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(fhirApiClient.getAppointments(anyString())).thenReturn(ResponseEntity.ok("{}"));
        when(fhirApiClient.getEncounters(anyString())).thenReturn(ResponseEntity.ok("{}"));
        when(fhirApiClient.getAllergies(anyString())).thenReturn(ResponseEntity.ok("{}"));
        when(fhirApiClient.getMedicationDispenses(anyString())).thenReturn(ResponseEntity.ok("{}"));
    }

    @Test
    void getPatientHealthSummary_ValidPatientId_ReturnsEmptySummary() {
        String patientId = "12724066";
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary(patientId);
        assertNotNull(response);
        assertNotNull(response.getRecentEncounters());
        assertNotNull(response.getActiveMedications());
        assertNotNull(response.getCriticalAllergies());
        assertNotNull(response.getUpcomingAppointments());
        assertNotNull(response.getErrors());
        assertTrue(response.getRecentEncounters().isEmpty());
        assertTrue(response.getActiveMedications().isEmpty());
        assertTrue(response.getCriticalAllergies().isEmpty());
        assertTrue(response.getUpcomingAppointments().isEmpty());
        assertTrue(response.getErrors().isEmpty());
    }
}
