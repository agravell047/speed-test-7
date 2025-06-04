# Interface Control Document (ICD)

## Overview

The Interface Control Document (ICD) outlines the API contracts for the HealthConnect Patient Health Summary application. It serves as a reference for developers and stakeholders to understand the expected behavior, request/response formats, and error handling of the API endpoints.

## API Endpoints

### 1. Heartbeat Endpoint

- **Endpoint**: `/heartbeat`
- **Method**: `GET`
- **Description**: This endpoint checks the health of the application.
- **Request**: No parameters required.
- **Response**:
  - **Status Code**: `200 OK`
  - **Content-Type**: `application/json`
  - **Response Body**:
    ```json
    {
      "status": "ok"
    }
    ```

## Error Handling

### Common Error Responses

- **400 Bad Request**
  - **Description**: The request was invalid.
  - **Response Body**:
    ```json
    {
      "error": "Bad Request",
      "message": "Detailed error message here."
    }
    ```

- **500 Internal Server Error**
  - **Description**: An unexpected error occurred on the server.
  - **Response Body**:
    ```json
    {
      "error": "Internal Server Error",
      "message": "Detailed error message here."
    }
    ```

## Versioning

- **Current Version**: 1.0
- **Last Updated**: [Insert Date]

## Change Log

- **Version 1.0**: Initial version of the ICD.