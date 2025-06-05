# Health Summary Application Architecture Diagram

## Overview

This document provides a visual representation of the architecture of the Health Summary Application. It outlines the key components, their interactions, and the overall structure of the application. The architecture now includes per-visit lab results, section-specific error reporting, and enhanced error handling for each data type.

## Components

- **Client Applications**: Frontend applications that interact with the backend service.
- **API Gateway**: Routes requests from clients to the appropriate backend services.
- **Health Summary Application**: The main backend service that aggregates and synthesizes patient data.
  - **Controllers**: Handle incoming HTTP requests and return responses.
    - **SummaryController**: Provides the patient health summary endpoint with per-visit aggregation and section-specific errors.
    - **HeartbeatController**: Provides a health check endpoint.
  - **Services**: Contain the business logic and interact with repositories and FHIR APIs.
  - **Repositories**: Interface with external data sources (e.g., Millennium Platform APIs).
- **Datadog Monitoring**: Monitors the application's performance and health.
- **Caching Layer**: Improves performance by storing frequently accessed data.
- **Database**: Stores persistent data related to patient health summaries.

## Diagram

```
+---------------------+
|   Client Applications|
+---------------------+
          |
          v
+---------------------+
|     API Gateway     |
+---------------------+
          |
          v
+---------------------+
| Health Summary App  |
| +-----------------+ |
| |  Controllers    | |
| |  - Summary      | |
| |  - Heartbeat    | |
| +-----------------+ |
| +-----------------+ |
| |    Services     | |
| +-----------------+ |
| +-----------------+ |
| |   Repositories  | |
| +-----------------+ |
+---------------------+
          |
          v
+---------------------+
|   Datadog Monitoring|
+---------------------+
          |
          v
+---------------------+
|     Caching Layer   |
+---------------------+
          |
          v
+---------------------+
|      Database       |
+---------------------+
```

## Key Flows

- Each patient summary request triggers aggregation of encounters, medications, allergies, lab results, and appointments.
- Section-specific errors are reported in the response for each data type (e.g., medications, labs).
- Per-visit lab results and follow-up appointments are included in each encounter summary.

## Conclusion

This architecture diagram serves as a guide for understanding the structure and flow of the Health Summary Application. It highlights the key components and their relationships, providing a clear overview of the system's design, including new per-visit lab results and section-specific error handling.
