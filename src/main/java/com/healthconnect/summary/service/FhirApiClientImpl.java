package com.healthconnect.summary.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
// Removed @Component and @RequiredArgsConstructor annotations to avoid double
// bean registration and constructor confusion
public class FhirApiClientImpl implements FhirApiClient {

    private final WebClient webClient;

    @Value("${healthconnect.fhir.base-url}")
    private String baseUrl;
    @Value("${healthconnect.fhir.appointment-path}")
    private String appointmentPath;
    @Value("${healthconnect.fhir.encounter-path}")
    private String encounterPath;
    @Value("${healthconnect.fhir.allergy-path}")
    private String allergyPath;
    @Value("${healthconnect.fhir.medication-dispense-path}")
    private String medicationDispensePath;

    public FhirApiClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ResponseEntity<String> getAppointments(String patientId) {
        String url = baseUrl + appointmentPath + "?patient=" + patientId + "&date=ge2024-06-03T00:00:00Z";
        return get(url);
    }

    @Override
    public ResponseEntity<String> getEncounters(String patientId) {
        String url = baseUrl + encounterPath + "?patient=" + patientId;
        return get(url);
    }

    @Override
    public ResponseEntity<String> getAllergies(String patientId) {
        String url = baseUrl + allergyPath + "?patient=" + patientId;
        return get(url);
    }

    @Override
    public ResponseEntity<String> getMedicationDispenses(String patientId) {
        String url = baseUrl + medicationDispensePath + "?patient=" + patientId;
        return get(url);
    }

    private ResponseEntity<String> get(String url) {
        try {
            String body = webClient.get()
                    .uri(url)
                    .header("Accept", "application/fhir+json")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return ResponseEntity.ok(body);
        } catch (WebClientResponseException ex) {
            log.error("FHIR API error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("FHIR API unexpected error: ", ex);
            return ResponseEntity.internalServerError().body("FHIR API call failed");
        }
    }
}
