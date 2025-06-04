# Health Summary App

This project is a backend service for HealthConnect's new patient portal and mobile application, providing a comprehensive "Patient Health Summary" dashboard. The backend aggregates, synthesizes, and standardizes data from Millennium Platform APIs, delivering a performant, reliable, and secure JSON response for diverse frontend systems.

## Table of Contents

- [Project Overview](#project-overview)
- [Key Features](#key-features)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [Testing](#testing)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

The Health Summary App is designed to provide patients with a summary of their health information, including recent encounters, active medications, critical allergies, and upcoming appointments. The application is built using Spring Boot 3.2 and Java 21, following best practices for backend development.

## Key Features

- **Recent Encounters Overview**: Displays the five most recent encounters with details such as encounter ID, date, reason for visit, primary diagnosis, and primary care provider name.
- **Active Medications Summary**: Lists all currently active medications with their names, dosages, routes, and start dates.
- **Critical Allergies List**: Provides a list of critical or severe allergies, including allergen names, reactions, and severity.
- **Upcoming Appointments**: Shows the next three appointments with details such as appointment ID, date and time, type of appointment, healthcare professional name, and location.

## Setup Instructions

1. **Clone the Repository**:
   ```
   git clone <repository-url>
   cd health-summary-app
   ```

2. **Build the Project**:
   ```
   mvn clean install
   ```

3. **Run the Application**:
   ```
   mvn spring-boot:run
   ```

4. **Access the API**:
   The application will be running at `http://localhost:8080`. You can access the heartbeat endpoint at `http://localhost:8080/heartbeat`.

## Usage

To check the health status of the application, send a GET request to the `/heartbeat` endpoint. The expected response is:
```json
{
  "status": "ok"
}
```

## Testing

Unit tests are included for the `HeartbeatController`. To run the tests, use the following command:
```
mvn test
```

## Documentation

For detailed API documentation, refer to the OpenAPI specifications and the Interface Control Document (ICD) located in the `docs` folder.

## Contributing

Contributions are welcome! Please follow the standard Git workflow for submitting changes.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.