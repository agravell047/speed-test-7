package com.healthconnect.summary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.summary.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Test
    void getPatientHealthSummary_AppointmentApiError_AddsError() {
        when(fhirApiClient.getAppointments(anyString())).thenReturn(ResponseEntity.status(500).body("error"));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertTrue(response.getErrors().stream().anyMatch(e -> e.getCode().contains("APPOINTMENT_API_ERROR")));
    }

    @Test
    void getPatientHealthSummary_AppointmentApiException_AddsError() {
        when(fhirApiClient.getAppointments(anyString())).thenThrow(new RuntimeException("fail"));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertTrue(response.getErrors().stream().anyMatch(e -> e.getCode().contains("APPOINTMENT_API_EXCEPTION")));
    }

    @Test
    void getPatientHealthSummary_EncounterApiError_AddsError() {
        when(fhirApiClient.getEncounters(anyString())).thenReturn(ResponseEntity.status(500).body("error"));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertTrue(response.getErrors().stream().anyMatch(e -> e.getCode().contains("ENCOUNTER_API_ERROR")));
    }

    @Test
    void getPatientHealthSummary_EncounterApiException_AddsError() {
        when(fhirApiClient.getEncounters(anyString())).thenThrow(new RuntimeException("fail"));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertTrue(response.getErrors().stream().anyMatch(e -> e.getCode().contains("ENCOUNTER_API_EXCEPTION")));
    }

    @Test
    void getPatientHealthSummary_AllergyApiError_AddsError() {
        when(fhirApiClient.getAllergies(anyString())).thenReturn(ResponseEntity.status(500).body("error"));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertTrue(response.getErrors().stream().anyMatch(e -> e.getCode().contains("ALLERGY_API_ERROR")));
    }

    @Test
    void getPatientHealthSummary_AllergyApiException_AddsError() {
        when(fhirApiClient.getAllergies(anyString())).thenThrow(new RuntimeException("fail"));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertTrue(response.getErrors().stream().anyMatch(e -> e.getCode().contains("ALLERGY_API_EXCEPTION")));
    }

    @Test
    void getPatientHealthSummary_MedicationApiError_AddsError() {
        when(fhirApiClient.getMedicationDispenses(anyString())).thenReturn(ResponseEntity.status(500).body("error"));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertTrue(response.getErrors().stream().anyMatch(e -> e.getCode().contains("MEDICATION_API_ERROR")));
    }

    @Test
    void getPatientHealthSummary_MedicationApiException_AddsError() {
        when(fhirApiClient.getMedicationDispenses(anyString())).thenThrow(new RuntimeException("fail"));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertTrue(response.getErrors().stream().anyMatch(e -> e.getCode().contains("MEDICATION_API_EXCEPTION")));
    }

    @Test
    void getPatientHealthSummary_MapsAppointmentsAndParticipants() throws Exception {
        String json = "{" +
                "\"entry\":[{" +
                "\"resource\":{\"id\":\"A1\",\"start\":\"2025-06-01T14:00:00Z\"," +
                "\"serviceType\":[{\"text\":\"Follow-up\"}]," +
                "\"participant\":[{" +
                "\"actor\":{\"reference\":\"Practitioner/1\",\"display\":\"Dr. John Doe\"}}," +
                "{\"actor\":{\"reference\":\"Location/1\",\"display\":\"Clinic A\"}}]}}]}";
        when(fhirApiClient.getAppointments(anyString())).thenReturn(ResponseEntity.ok(json));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertEquals(1, response.getUpcomingAppointments().size());
        UpcomingAppointmentDto dto = response.getUpcomingAppointments().get(0);
        assertEquals("A1", dto.getAppointmentId());
        assertEquals("Dr. John Doe", dto.getProvider());
        assertEquals("Clinic A", dto.getLocation());
        assertEquals("Follow-up", dto.getType());
    }

    @Test
    void getPatientHealthSummary_MapsEncountersWithReasonDiagnosisProvider() throws Exception {
        String json = "{" +
                "\"entry\":[{" +
                "\"resource\":{\"id\":\"E1\",\"period\":{\"start\":\"2025-05-27T09:00:00Z\"}," +
                "\"reasonCode\":[{\"text\":\"Annual Checkup\"}]," +
                "\"diagnosis\":[{\"condition\":{\"display\":\"Hypertension\"}}]," +
                "\"participant\":[{\"type\":[{\"text\":\"Primary\"}],\"individual\":{\"display\":\"Dr. Jane Smith\"}}]}}]}";
        when(fhirApiClient.getEncounters(anyString())).thenReturn(ResponseEntity.ok(json));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertEquals(1, response.getRecentEncounters().size());
        RecentEncounterDto dto = response.getRecentEncounters().get(0);
        assertEquals("E1", dto.getEncounterId());
        assertEquals("Annual Checkup", dto.getReasonForVisit());
        assertEquals("Hypertension", dto.getPrimaryDiagnosis());
        assertEquals("Dr. Jane Smith", dto.getPrimaryCareProvider());
    }

    @Test
    void getPatientHealthSummary_MapsCriticalAllergies() throws Exception {
        String json = "{" +
                "\"entry\":[{" +
                "\"resource\":{\"id\":\"AL1\",\"code\":{\"text\":\"Penicillin\"}," +
                "\"reaction\":[{\"severity\":\"critical\",\"description\":\"Anaphylaxis\"}]}}]}";
        when(fhirApiClient.getAllergies(anyString())).thenReturn(ResponseEntity.ok(json));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertEquals(1, response.getCriticalAllergies().size());
        CriticalAllergyDto dto = response.getCriticalAllergies().get(0);
        assertEquals("Penicillin", dto.getAllergenName());
        assertEquals("Anaphylaxis", dto.getReaction());
        assertEquals("Critical", dto.getSeverity());
    }

    @Test
    void getPatientHealthSummary_MapsActiveMedications() throws Exception {
        String json = "{" +
                "\"entry\":[{" +
                "\"resource\":{\"medicationCodeableConcept\":{\"text\":\"Lisinopril\"}," +
                "\"whenHandedOver\":\"2024-12-01T00:00:00Z\"," +
                "\"dosageInstruction\":[{\"text\":\"10mg\",\"route\":{\"text\":\"Oral\"}}]," +
                "\"status\":\"completed\"}}]}";
        when(fhirApiClient.getMedicationDispenses(anyString())).thenReturn(ResponseEntity.ok(json));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertEquals(1, response.getActiveMedications().size());
        ActiveMedicationDto dto = response.getActiveMedications().get(0);
        assertEquals("Lisinopril", dto.getMedicationName());
        assertEquals("10mg", dto.getDosage());
        assertEquals("Oral", dto.getRoute());
        assertEquals("2024-12-01", dto.getStartDate());
    }

    @Test
    void getPatientHealthSummary_InvalidJson_HandledGracefully() {
        when(fhirApiClient.getAppointments(anyString())).thenReturn(ResponseEntity.ok("not-json"));
        PatientHealthSummaryResponse response = patientSummaryService.getPatientHealthSummary("id");
        assertFalse(response.getErrors().isEmpty());
    }

    @Test
    void fallbackPatientHealthSummary_ReturnsFallbackResponse() {
        PatientHealthSummaryResponse response = patientSummaryService.fallbackPatientHealthSummary("id", new RuntimeException("fail"));
        assertNotNull(response);
        assertTrue(response.getRecentEncounters().isEmpty());
        assertTrue(response.getActiveMedications().isEmpty());
        assertTrue(response.getCriticalAllergies().isEmpty());
        assertTrue(response.getUpcomingAppointments().isEmpty());
        assertEquals(1, response.getErrors().size());
        assertEquals("SUMMARY_FALLBACK", response.getErrors().get(0).getCode());
    }
}
