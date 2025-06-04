package com.healthconnect.summary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/heartbeat", "/actuator/health").permitAll()
                        .requestMatchers("/api/patients/*/summary").permitAll() // Allow summary endpoint for now
                        .anyRequest().authenticated())
                .httpBasic(httpBasic -> {
                });
        return http.build();
    }
}
