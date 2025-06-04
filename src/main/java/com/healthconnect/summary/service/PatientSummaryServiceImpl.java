package com.healthconnect.summary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.summary.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of PatientSummaryService that aggregates and synthesizes
 * patient data from FHIR APIs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PatientSummaryServiceImpl implements PatientSummaryService {

    private final FhirApiClient fhirApiClient;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    @Cacheable(value = "patientSummary", key = "#patientId")
    @CircuitBreaker(name = "patientSummaryCB", fallbackMethod = "fallbackPatientHealthSummary")
    @Retry(name = "patientSummaryRetry")
    public PatientHealthSummaryResponse getPatientHealthSummary(String patientId) {
        List<RecentEncounterDto> recentEncounters = new ArrayList<>();
        List<ActiveMedicationDto> activeMedications = new ArrayList<>();
        List<CriticalAllergyDto> criticalAllergies = new ArrayList<>();
        List<UpcomingAppointmentDto> upcomingAppointments = new ArrayList<>();
        List<ErrorDto> errors = new ArrayList<>();

        // Fetch and map appointments
        try {
            ResponseEntity<String> appointmentResponse = fhirApiClient.getAppointments(patientId);
            if (appointmentResponse.getStatusCode().is2xxSuccessful() && appointmentResponse.getBody() != null) {
                JsonNode bundle = OBJECT_MAPPER.readTree(appointmentResponse.getBody());
                if (bundle.has("entry")) {
                    List<UpcomingAppointmentDto> appointmentList = new ArrayList<>();
                    for (JsonNode entry : bundle.get("entry")) {
                        JsonNode resource = entry.get("resource");
                        if (resource != null && resource.has("id") && resource.has("start")) {
                            String appointmentId = resource.get("id").asText();
                            String dateTime = resource.path("start").asText("");
                            if (!dateTime.isEmpty()) {
                                try {
                                    OffsetDateTime odt = OffsetDateTime.parse(dateTime);
                                    dateTime = odt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                                } catch (Exception ignored) {
                                }
                            }
                            String type = resource.path("serviceType").isArray()
                                    && resource.path("serviceType").size() > 0
                                            ? resource.path("serviceType").get(0).path("text").asText("")
                                            : "";
                            String provider = "";
                            String location = "";
                            if (resource.has("participant") && resource.get("participant").isArray()) {
                                for (JsonNode part : resource.get("participant")) {
                                    String actorType = part.path("actor").path("reference").asText("");
                                    String display = part.path("actor").path("display").asText("");
                                    if (actorType.startsWith("Practitioner")) {
                                        provider = display;
                                    } else if (actorType.startsWith("Location")) {
                                        location = display;
                                    }
                                }
                            }
                            UpcomingAppointmentDto dto = UpcomingAppointmentDto.builder()
                                    .appointmentId(appointmentId)
                                    .dateTime(dateTime)
                                    .type(type)
                                    .provider(provider)
                                    .location(location)
                                    .build();
                            appointmentList.add(dto);
                        }
                    }
                    // Sort by date ascending, limit 3
                    appointmentList.sort(Comparator.comparing(UpcomingAppointmentDto::getDateTime,
                            Comparator.nullsLast(Comparator.naturalOrder())));
                    upcomingAppointments.addAll(appointmentList.stream().limit(3).toList());
                }
            } else {
                errors.add(ErrorDto.builder().code("APPOINTMENT_API_ERROR").message("Failed to fetch appointments")
                        .build());
            }
        } catch (Exception ex) {
            log.error("Error fetching appointments", ex);
            errors.add(ErrorDto.builder().code("APPOINTMENT_API_EXCEPTION").message("Exception fetching appointments")
                    .build());
        }

        // Fetch and map encounters
        try {
            ResponseEntity<String> encounterResponse = fhirApiClient.getEncounters(patientId);
            if (encounterResponse.getStatusCode().is2xxSuccessful() && encounterResponse.getBody() != null) {
                JsonNode bundle = OBJECT_MAPPER.readTree(encounterResponse.getBody());
                if (bundle.has("entry")) {
                    List<RecentEncounterDto> encounterList = new ArrayList<>();
                    for (JsonNode entry : bundle.get("entry")) {
                        JsonNode resource = entry.get("resource");
                        if (resource != null && resource.has("id") && resource.has("period")) {
                            String encounterId = resource.get("id").asText();
                            String date = resource.path("period").path("start").asText("");
                            // Standardize date format
                            if (!date.isEmpty()) {
                                try {
                                    OffsetDateTime odt = OffsetDateTime.parse(date);
                                    date = odt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                                } catch (Exception ignored) {
                                }
                            }
                            String reasonForVisit = "Not specified";
                            if (resource.has("reasonCode") && resource.get("reasonCode").isArray()
                                    && resource.get("reasonCode").size() > 0) {
                                JsonNode reason = resource.get("reasonCode").get(0);
                                reasonForVisit = reason.path("text").asText("Not specified");
                            }
                            String primaryDiagnosis = "";
                            if (resource.has("diagnosis") && resource.get("diagnosis").isArray()
                                    && resource.get("diagnosis").size() > 0) {
                                JsonNode diag = resource.get("diagnosis").get(0);
                                primaryDiagnosis = diag.path("condition").path("display").asText("");
                            }
                            String primaryCareProvider = "";
                            if (resource.has("participant") && resource.get("participant").isArray()) {
                                for (JsonNode part : resource.get("participant")) {
                                    String type = part.path("type").toString().toLowerCase();
                                    if (type.contains("primary")) {
                                        primaryCareProvider = part.path("individual").path("display").asText("");
                                        break;
                                    }
                                }
                            }
                            RecentEncounterDto dto = RecentEncounterDto.builder()
                                    .encounterId(encounterId)
                                    .date(date)
                                    .reasonForVisit(reasonForVisit)
                                    .primaryDiagnosis(primaryDiagnosis)
                                    .primaryCareProvider(primaryCareProvider)
                                    .build();
                            encounterList.add(dto);
                        }
                    }
                    // Sort by date descending, limit 5
                    encounterList.sort(Comparator.comparing(RecentEncounterDto::getDate,
                            Comparator.nullsLast(Comparator.reverseOrder())));
                    recentEncounters.addAll(encounterList.stream().limit(5).toList());
                }
            } else {
                errors.add(
                        ErrorDto.builder().code("ENCOUNTER_API_ERROR").message("Failed to fetch encounters").build());
            }
        } catch (Exception ex) {
            log.error("Error fetching encounters", ex);
            errors.add(ErrorDto.builder().code("ENCOUNTER_API_EXCEPTION").message("Exception fetching encounters")
                    .build());
        }

        // Fetch and map allergies
        try {
            ResponseEntity<String> allergyResponse = fhirApiClient.getAllergies(patientId);
            if (allergyResponse.getStatusCode().is2xxSuccessful() && allergyResponse.getBody() != null) {
                JsonNode bundle = OBJECT_MAPPER.readTree(allergyResponse.getBody());
                if (bundle.has("entry")) {
                    for (JsonNode entry : bundle.get("entry")) {
                        JsonNode resource = entry.get("resource");
                        if (resource != null && resource.has("id") && resource.has("code")) {
                            String severity = "";
                            if (resource.has("reaction") && resource.get("reaction").isArray()
                                    && resource.get("reaction").size() > 0) {
                                JsonNode reaction = resource.get("reaction").get(0);
                                severity = reaction.path("severity").asText("");
                            }
                            if ("severe".equalsIgnoreCase(severity) || "critical".equalsIgnoreCase(severity)) {
                                String allergenName = resource.path("code").path("text").asText("");
                                String reaction = "";
                                if (resource.has("reaction") && resource.get("reaction").isArray()
                                        && resource.get("reaction").size() > 0) {
                                    JsonNode react = resource.get("reaction").get(0);
                                    reaction = react.path("description").asText("");
                                }
                                CriticalAllergyDto dto = CriticalAllergyDto.builder()
                                        .allergenName(allergenName)
                                        .reaction(reaction)
                                        .severity(severity.substring(0, 1).toUpperCase()
                                                + severity.substring(1).toLowerCase())
                                        .build();
                                criticalAllergies.add(dto);
                            }
                        }
                    }
                }
            } else {
                errors.add(ErrorDto.builder().code("ALLERGY_API_ERROR").message("Failed to fetch allergies").build());
            }
        } catch (Exception ex) {
            log.error("Error fetching allergies", ex);
            errors.add(
                    ErrorDto.builder().code("ALLERGY_API_EXCEPTION").message("Exception fetching allergies").build());
        }

        // Fetch and map medications
        try {
            ResponseEntity<String> medResponse = fhirApiClient.getMedicationDispenses(patientId);
            if (medResponse.getStatusCode().is2xxSuccessful() && medResponse.getBody() != null) {
                JsonNode bundle = OBJECT_MAPPER.readTree(medResponse.getBody());
                if (bundle.has("entry")) {
                    for (JsonNode entry : bundle.get("entry")) {
                        JsonNode resource = entry.get("resource");
                        if (resource != null && resource.has("medicationCodeableConcept")) {
                            String medicationName = resource.path("medicationCodeableConcept").path("text").asText("");
                            String dosage = "";
                            String route = "";
                            String startDate = resource.path("whenHandedOver").asText("");
                            if (!startDate.isEmpty()) {
                                try {
                                    OffsetDateTime odt = OffsetDateTime.parse(startDate);
                                    startDate = odt.format(DateTimeFormatter.ISO_LOCAL_DATE);
                                } catch (Exception ignored) {
                                }
                            }
                            if (resource.has("dosageInstruction") && resource.get("dosageInstruction").isArray()
                                    && resource.get("dosageInstruction").size() > 0) {
                                JsonNode dosageNode = resource.get("dosageInstruction").get(0);
                                dosage = dosageNode.path("text").asText("");
                                if (dosageNode.has("route")) {
                                    route = dosageNode.path("route").path("text").asText("");
                                }
                            }
                            // Only add if status is "completed" or "in-progress" (active)
                            String status = resource.path("status").asText("");
                            if ("completed".equalsIgnoreCase(status) || "in-progress".equalsIgnoreCase(status)) {
                                ActiveMedicationDto dto = ActiveMedicationDto.builder()
                                        .medicationName(medicationName)
                                        .dosage(dosage)
                                        .route(route)
                                        .startDate(startDate)
                                        .build();
                                activeMedications.add(dto);
                            }
                        }
                    }
                }
            } else {
                errors.add(
                        ErrorDto.builder().code("MEDICATION_API_ERROR").message("Failed to fetch medications").build());
            }
        } catch (Exception ex) {
            log.error("Error fetching medications", ex);
            errors.add(ErrorDto.builder().code("MEDICATION_API_EXCEPTION").message("Exception fetching medications")
                    .build());
        }

        return PatientHealthSummaryResponse.builder()
                .recentEncounters(recentEncounters)
                .activeMedications(activeMedications)
                .criticalAllergies(criticalAllergies)
                .upcomingAppointments(upcomingAppointments)
                .errors(errors)
                .build();
    }

    public PatientHealthSummaryResponse fallbackPatientHealthSummary(String patientId, Throwable t) {
        log.error("Fallback triggered for patient summary: {}", patientId, t);
        return PatientHealthSummaryResponse.builder()
                .recentEncounters(List.of())
                .activeMedications(List.of())
                .criticalAllergies(List.of())
                .upcomingAppointments(List.of())
                .errors(List.of(
                        ErrorDto.builder().code("SUMMARY_FALLBACK").message("Service temporarily unavailable").build()))
                .build();
    }
}
