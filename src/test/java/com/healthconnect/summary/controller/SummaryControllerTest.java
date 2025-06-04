package com.healthconnect.summary.controller;

import com.healthconnect.summary.dto.PatientHealthSummaryResponse;
import com.healthconnect.summary.service.PatientSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SummaryControllerTest {

    @Mock
    private PatientSummaryService patientSummaryService;

    @InjectMocks
    private SummaryController summaryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPatientSummary_ValidPatientId_ReturnsSummary() {
        String patientId = "12724066";
        PatientHealthSummaryResponse mockResponse = new PatientHealthSummaryResponse();
        when(patientSummaryService.getPatientHealthSummary(patientId)).thenReturn(mockResponse);

        ResponseEntity<PatientHealthSummaryResponse> response = summaryController.getPatientSummary(patientId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(patientSummaryService).getPatientHealthSummary(patientId);
    }
}
