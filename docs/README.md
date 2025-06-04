# Health Summary App

## Overview
The Health Summary App is a backend service designed for HealthConnect's patient portal and mobile application. It aggregates and synthesizes patient data from various sources to provide a comprehensive "Patient Health Summary" dashboard.

## Features
- **Heartbeat Endpoint**: A simple health check endpoint to verify that the service is running.
- **Data Aggregation**: Collects and standardizes patient data from Millennium Platform APIs.
- **Performance**: Optimized for low latency and high throughput.

## Getting Started

### Prerequisites
- Java 21
- Maven 3.6 or higher

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/yourusername/health-summary-app.git
   ```
2. Navigate to the project directory:
   ```
   cd health-summary-app
   ```
3. Build the project:
   ```
   mvn clean install
   ```

### Running the Application
To run the application, use the following command:
```
mvn spring-boot:run
```
The application will start on `http://localhost:8080`.

### API Endpoints
- **GET /heartbeat**: Returns a JSON response indicating the service status.
  - **Response**:
    ```json
    {
      "status": "ok"
    }
    ```

## Testing
Unit tests are included in the project to ensure functionality. To run the tests, execute:
```
mvn test
```

## Documentation
- For detailed API documentation, refer to the OpenAPI specifications.
- The Interface Control Document (ICD) is available in the `docs` folder.

## Postman Collection
A Postman collection for testing the API endpoints is available in the `postman` directory.

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for discussion.

## License
This project is licensed under the MIT License. See the LICENSE file for details.