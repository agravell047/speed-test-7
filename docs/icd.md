# Interface Control Document (ICD)

## Overview

The Interface Control Document (ICD) outlines the API contracts for the HealthConnect Patient Health Summary application. It serves as a reference for developers and stakeholders to understand the expected behavior, request/response formats, and error handling of the API endpoints.

## API Endpoints

### 1. Patient Health Summary Endpoint

- **Endpoint**: `/api/patients/{patientId}/summary`
- **Method**: `GET`
- **Description**: Returns a synthesized health summary for the given patient, including recent encounters, medications, allergies, lab results, and appointments. Section-specific errors are reported for each data type.
- **Request**:
  - **Path Parameter**: `patientId` (string, required)
- **Response**:
  - **Status Code**: `200 OK`
  - **Content-Type**: `application/json`
  - **Response Body**:
    ```json
    {
      "recentEncounters": [
        {
          "encounterId": "string",
          "date": "2025-05-27T09:00:00Z",
          "reasonForVisit": "string",
          "primaryDiagnosis": "string",
          "primaryCareProvider": "string",
          "medications": [
            {
              /* ActiveMedicationDto */
            }
          ],
          "labResults": [
            {
              /* LabResultDto */
            }
          ],
          "followUpAppointments": [
            {
              /* UpcomingAppointmentDto */
            }
          ],
          "medicationsError": { "code": "string", "message": "string" },
          "labsError": { "code": "string", "message": "string" },
          "followUpsError": { "code": "string", "message": "string" }
        }
      ],
      "activeMedications": [
        {
          /* ActiveMedicationDto */
        }
      ],
      "criticalAllergies": [
        {
          /* CriticalAllergyDto */
        }
      ],
      "upcomingAppointments": [
        {
          /* UpcomingAppointmentDto */
        }
      ],
      "errors": [{ "code": "string", "message": "string" }],
      "appointmentsError": { "code": "string", "message": "string" },
      "encountersError": { "code": "string", "message": "string" },
      "allergiesError": { "code": "string", "message": "string" },
      "medicationsError": { "code": "string", "message": "string" },
      "labsError": { "code": "string", "message": "string" }
    }
    ```

## Error Handling

Section-specific errors are reported in the response for each data type (e.g., `medicationsError`, `labsError`).

### Common Error Responses

- **400 Bad Request**

  - **Description**: The request was invalid.
  - **Response Body**:
    ```json
    {
      "errors": [
        { "code": "BAD_REQUEST", "message": "Detailed error message here." }
      ]
    }
    ```

- **404 Not Found**

  - **Description**: Patient not found or no data available.
  - **Response Body**:
    ```json
    {
      "errors": [
        { "code": "NOT_FOUND", "message": "No data found for patient." }
      ]
    }
    ```

- **500 Internal Server Error**
  - **Description**: An unexpected error occurred on the server.
  - **Response Body**:
    ```json
    {
      "errors": [
        { "code": "INTERNAL_ERROR", "message": "Detailed error message here." }
      ]
    }
    ```

## Versioning

- **Current Version**: 1.1
- **Last Updated**: 2025-06-03

## Change Log

- **Version 1.1**: Added section-specific errors and lab results to the patient summary response.
- **Version 1.0**: Initial version of the ICD.
