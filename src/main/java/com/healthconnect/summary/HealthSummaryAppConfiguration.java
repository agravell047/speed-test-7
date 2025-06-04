package com.healthconnect.summary;

import com.healthconnect.summary.service.FhirApiClient;
import com.healthconnect.summary.service.FhirApiClientImpl;
import com.healthconnect.summary.service.PatientSummaryService;
import com.healthconnect.summary.service.PatientSummaryServiceImpl;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableCaching
public class HealthSummaryAppConfiguration {

    @Bean
    public PatientSummaryService patientSummaryService(FhirApiClient fhirApiClient) {
        return new PatientSummaryServiceImpl(fhirApiClient);
    }

    @Bean
    public FhirApiClient fhirApiClient(WebClient.Builder webClientBuilder) {
        return new FhirApiClientImpl(webClientBuilder.build());
    }
}