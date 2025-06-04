package com.healthconnect.summary.service;

import org.springframework.http.ResponseEntity;

public interface FhirApiClient {
    ResponseEntity<String> getAppointments(String patientId);

    ResponseEntity<String> getEncounters(String patientId);

    ResponseEntity<String> getAllergies(String patientId);

    ResponseEntity<String> getMedicationDispenses(String patientId);
}
