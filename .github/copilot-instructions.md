# Copilot Instructions

This project is a backend service for HealthConnect's new patient portal and mobile application, providing a comprehensive "Patient Health Summary" dashboard. The backend must aggregate, synthesize, and standardize data from Millennium Platform APIs, delivering a performant, reliable, and secure JSON response for diverse frontend systems.

## 🧠 Context & Problem Domain

- **Purpose**: Aggregate and synthesize patient data from Millennium Platform APIs to power the "Patient Health Summary" dashboard.
- **Key Features**:
  - **Recent Encounters Overview**: Five most recent encounters, each with:
    - Encounter ID
    - Encounter Date
    - Reason for Visit (standardized; fallback: "Not specified")
    - Primary Diagnosis (linked from clinical events/problem lists)
    - Primary Care Provider Name (linked from care team)
  - **Active Medications Summary**: All currently active medications, each with:
    - Medication Name
    - Dosage
    - Route
    - Start Date
  - **Critical Allergies List**: All allergies classified as "critical" or "severe", each with:
    - Allergen Name
    - Reaction
    - Severity
  - **Upcoming Appointments**: Next three appointments, each with:
    - Appointment ID
    - Appointment Date and Time
    - Type of Appointment
    - Healthcare Professional Name
    - Location
- **Data Consistency**: Handle Millennium API data inconsistencies (e.g., reason for visit variations) and provide standardized output.
- **Performance**: Optimize for low latency and high throughput; use efficient data retrieval, caching, and processing.
- **Reliability**: Degrade gracefully on API errors; provide consistent, well-formed JSON responses with error objects (never stack traces).
- **Security & Privacy**: Adhere to strict security protocols; assume authentication/authorization is enforced for all API access.
- **Scalability**: Design for high concurrency and large-scale patient access.

## 🧩 Architecture & Patterns

- **Layered Architecture**: Controller → Service → Repository → (Millennium API Client/Cache)
- **DTOs**: Use explicit DTOs for all API responses. Never expose Millennium/FHIR/EHR models directly.
- **Resilience**: Implement circuit breakers, retries, and timeouts for all Millennium API calls. Log and surface errors in a standardized way.
- **Caching**: Use in-memory or distributed caching for recent patient data to improve reliability and performance.
- **Testing**: Mock Millennium APIs in tests. Cover success, error, fallback, and degraded scenarios.
- **Observability**: Instrument endpoints with metrics (Micrometer), health checks (Actuator), and logging (SLF4J/Logback). Add Datadog monitors for all critical endpoints.

## 🔧 Implementation Guidelines

- **API Design**: Expose a REST endpoint like `/api/patients/{patientId}/summary`.
- **Standardized JSON Response Example**:
  ```json
  {
    "recentEncounters": [
      {
        "encounterId": "12345",
        "date": "2025-05-27T09:00:00Z",
        "reasonForVisit": "Annual Checkup",
        "primaryDiagnosis": "Hypertension",
        "primaryCareProvider": "Dr. Jane Smith"
      }
    ],
    "activeMedications": [
      {
        "medicationName": "Lisinopril",
        "dosage": "10mg",
        "route": "Oral",
        "startDate": "2024-12-01"
      }
    ],
    "criticalAllergies": [
      {
        "allergenName": "Penicillin",
        "reaction": "Anaphylaxis",
        "severity": "Critical"
      }
    ],
    "upcomingAppointments": [
      {
        "appointmentId": "A9876",
        "dateTime": "2025-06-01T14:00:00Z",
        "type": "Follow-up",
        "provider": "Dr. John Doe",
        "location": "Clinic A"
      }
    ],
    "errors": []
  }
  ```
- **Error Handling**: If Millennium APIs are unavailable or data is incomplete, return a clear error object in the JSON response (never HTTP 500 with a stack trace). Document all error shapes in OpenAPI.
- **Swagger/OpenAPI**: Annotate all endpoints and error responses. Keep docs up to date.
- **Unit/Integration Tests**: For every code change, add/maintain tests for success, Millennium API failure, and degraded/fallback scenarios.
- **Postman Collection**: Include requests for all endpoints, including error/fallback cases.
- **Datadog Monitors**: Add monitors for summary endpoint availability and error rates.

## 🧶 Additional Patterns

- Use constructor injection for all dependencies.
- Never expose Millennium/FHIR/EHR models directly to the API.
- Use `@Slf4j` for logging, never `System.out.println`.
- Use `@RestControllerAdvice` for global exception handling.
- Use Java Bean Validation for all input.
- Prefer explicit DTOs and avoid generic types in API responses.
- Document all error and fallback behaviors in OpenAPI and Postman.

## 📝 Workflow

- Use AI tools for code generation, refactoring, and test creation.
- All code must be covered by tests (success, error, fallback).
- All endpoints must be observable (metrics, logs, monitors).
- All documentation (README, ICD, diagrams, Postman) must be kept up to date with every change.

## General Guidelines

- Use Java-idiomatic patterns and best practices.
- Prefer readability over cleverness.
- Use Lombok annotations (e.g., @Getter, @Setter, @Slf4j) for DTOs and POJOs to minimize boilerplate, but avoid @Data and only generate methods that are actually needed and used. Do not generate equals, hashCode, or toString unless explicitly required and tested.
- Prefer Lombok for DTOs, POJOs, and logging instead of writing boilerplate code manually.
- Each class will be placed in its own file, following the standard Java convention and your project structure.
- Configuration fields (like @Value and @Autowired) will be placed at the top of the class, after the class declaration and before methods, not in the middle or bottom.
- No nested classes inside service or other classes; all DTOs and helper classes will be in their own files in the appropriate package (e.g., dto).
- For every code change (new feature, method, or update), always create or update corresponding unit and/or integration tests. This is required even if the prompt does not explicitly mention tests. Code changes without tests are not permitted.

## Spring Boot Application Structure

- Base package name should match group ID in `pom.xml` (e.g., `com.example.healthconnect`).
- Spring Boot Application and Configuration classes should reside in base package and be named based on the artifact ID minus postfixes of either `app` or `api` (e.g., `HealthSummaryApplication`, `HealthSummaryConfiguration`).
- Use `@SpringBootApplication` for the main application class.
- Use `@Configuration` for configuration classes.
- Use `@RestController` for API controllers.
- Use `@Service` for service classes.
- Use `@Repository` for repository classes.
- Use `@Entity` for JPA entities.
- Use `@Data` from Lombok for DTOs and POJOs to reduce boilerplate code.
- Use `@Slf4j` from Lombok for logging in classes.
- Use `@Value` for configuration properties.
- Use Constructor injection for dependencies whenever possible.

## Maven Coordinates and Naming Conventions

- **Maven Coordinates**: The project must use the following Maven coordinates:
  - `groupId`: com.healthconnect.summary
  - `artifactId`: health-summary-app
  - `version`: 1.0-SNAPSHOT
- **Naming Conventions**:
  - The main Spring Boot application class must be named based on the artifactId, using PascalCase and omitting dashes (e.g., `HealthSummaryAppApplication`).
  - Configuration classes should follow the same convention (e.g., `HealthSummaryAppConfiguration`).
  - All Java packages must start with the groupId (e.g., `com.healthconnect.summary`).

## Java Coding Standards

- Use camelCase for variable, method, and parameter names.
- Use PascalCase for class and interface names.
- Use 4 spaces for indentation.
- Use braces `{}` for all code blocks, even single-line blocks.
- Use `final` for constants and variables that should not be reassigned.
- Use descriptive names for classes, methods, and variables.
- Place each class in its own file.
- Use Javadoc comments for public classes and methods.
- Organize imports: standard Java, third-party, then project-specific. Avoid wildcard imports.
- Keep lines under 120 characters.
- Use annotations (e.g., `@Override`, `@RestController`) where appropriate.
- Follow standard Java conventions for spacing and formatting.
- Include short, helpful code comments only when necessary to explain complex code; avoid excessive or redundant comments. Comment on the "why" rather than the "how".
- Constants should be UPPER_SNAKE_CASE.
- Prefer explicit types for local variables instead of var. This ensures clarity, compatibility, and consistency across the codebase.

## 📁 File Structure

Use this structure as a guide when creating or updating files:

```
src/
  main/
    java/
      com/healthconnect/summary/
        controller/
        service/
        dto/
  test/
    java/
      com/healthconnect/summary/
        controller/
        service/
        ...
datadog/
  [module]/
    [endpoint]-monitor.json
docs/
  [name]-diagram.md
  icd.md
  README.md
postman/
  [name].postman_collection.json
prompt-history/
  prompt-history.md
```

## Patterns

### ✅ Patterns to Follow

- Organize code by layer and feature
- Use Dependency Injection for services and repositories.
- Use Repository Pattern for data access.
- For APIs:
  - Input validation using Java Bean Validation (e.g., `@Valid`, `@NotNull`).
  - Error handling using custom exceptions and status codes.
  - Logging via SLF4J or similar logging frameworks.
  - Ensure Swagger/OpenAPI documentation is kept up to date as controllers are added or changed.
- Always accompany new or changed code with appropriate tests in the correct test package.

### 🚫 Patterns to Avoid

- Don't generate code without tests.
- Never submit or commit code changes without corresponding tests.
- Avoid global state unless absolutely necessary.
- Don't expose secrets or keys.
- Avoid SELECT \* in queries; specify fields explicitly.
- Don't use wildcard imports.
- Don't include unnecessary or redundant comments.

## Exception Handling

- Handle exceptions explicitly.
- Catch and assert exceptions in tests using `assertThrows`.
- Use custom exceptions (e.g., `HealthConnectSummaryRuntimeException`) for business logic errors.

## API Design & Documentation

- Annotate all Java REST endpoints with Swagger/OpenAPI annotations (e.g., `@Operation`, `@ApiResponse`, `@Tag`) to generate API documentation.
- Use WebClient instead of RestTemplate for making external API calls.
- Avoid generic types whenever possible; use specific DTOs for clarity.
- Ensure Swagger/OpenAPI documentation is kept up to date as controllers are added or changed.
- When adding or updating error handling in API controllers, always update Swagger/OpenAPI annotations to document error responses (e.g., @ApiResponse for 500 errors) so the API documentation remains accurate.
- Whenever possible, implement robust error handling in your code to ensure reliable, fault-tolerant API controllers. Use appropriate exception handling, return meaningful error responses, and log errors for troubleshooting.

## Testing Guidelines

- Use JUnit 5 and Mockito for unit tests.
- Write tests using JUnit 5 annotations (`@Test`, `@BeforeEach`, `@ExtendWith(MockitoExtension.class)`, etc.).
- Use Mockito for mocking dependencies and `@InjectMocks` for the service under test.
- Use `@MockitoSettings(strictness = Strictness.LENIENT)` where needed.
- For every code change, write all related tests in a test file—even if not explicitly requested in the prompt.
- Write descriptive test method names using the pattern `testMethodName_Condition_ExpectedResult` or similar.
- Mock external dependencies in tests using `when(...).thenReturn(...)` or `doThrow(...)` for repository and service calls.
- Use `verify(...)` to assert interactions with mocks.
- Use constructor injection or `@InjectMocks` for services in tests.
- When adding or updating error handling in API controllers, always update or add tests to cover error scenarios and ensure error handling works as expected.
- Include mocks/stubs for third-party services.
- Use the Maven JaCoCo plugin to capture code coverage. Code coverage must be a minimum of 80% across all packages in the project.

## End-to-End Testing & Automation

- The Postman collection is automatically run using Newman in CI to ensure all endpoints work as expected.
- Test results are available in the CI pipeline output.

## Project Documentation & Artifacts

- Update the README.md file whenever necessary to reflect changes in features, endpoints, setup, usage, or documentation.
- Postman collection files are located in the `postman` folder. When adding or updating endpoints, ensure the Postman collection is updated accordingly.
- When API endpoints are added or changed, update the Mermaid diagram in `docs/[ProjectName]-diagram.md` to reflect the current API structure.
- When interface details or API contracts change, update the Interface Control Document (ICD) in the `docs` folder (e.g., `docs/icd.md`).
- The `docs` folder contains the Interface Control Document (ICD), API diagrams, and other technical documentation related to the project interfaces and architecture. Update these files as needed to keep documentation accurate and current.

## Workflow and Automation Instructions

- For commit messages, keep it extremely short but detailed with the file changes and the reason for the change. Don't use emojis.
- When asked to commit staged files, automatically generate a descriptive commit message summarizing the changes, and use it in the commit command.
  - If the user mentions "fix", "fixes", or "fixing", use a commit message starting with `fix:` followed by a summary of the fix.
  - If the user mentions "feature", "add", or "adding", use a commit message starting with `feat:` followed by a summary of the feature.
  - If the user mentions "refactor", use a commit message starting with `refactor:` followed by a summary of the refactor.
  - If the user mentions "docs" or documentation, use a commit message starting with `docs:` followed by a summary of the documentation change.
  - If the user mentions "test" or "tests", use a commit message starting with `test:` followed by a summary of the test change.
  - Otherwise, generate a clear and descriptive commit message based on the context.
- When asked to push, always check the current branch and push to that branch.
- When asked to create a branch, automatically generate a branch name based on the context of the request, using dashes to separate words (e.g., `feature-add-summary-endpoint`).
  - If the user mentions "fix", "fixes", or "fixing", use a branch name starting with `fix-` followed by a context-based description (e.g., `fix-bug-in-summary-controller`).
  - If the user mentions "feature", "add", or "adding", use a branch name starting with `feature-` followed by a context-based description (e.g., `feature-add-summary-endpoint`).
  - If the user mentions "refactor", use a branch name starting with `refactor-` followed by a context-based description (e.g., `refactor-summary-service-logic`).
  - If the user mentions "docs" or documentation, use a branch name starting with `docs-` followed by a context-based description (e.g., `docs-update-readme`).
  - If the user mentions "test" or "tests", use a branch name starting with `test-` followed by a context-based description (e.g., `test-add-summary-service-tests`).
  - Otherwise, generate a clear and descriptive branch name based on the context, using dashes to separate words.
- Always update the `.gitignore` file with any new files, folders, or patterns that should not be tracked by git as they are added to this repository.

## Monitoring & Observability

- When a new API endpoint is added or an existing endpoint is changed, create or update a corresponding Datadog monitor JSON file in the `/datadog` folder. Each monitor file should be named after the endpoint (e.g., `[ControllerName]-get-monitor.json`) and reflect the latest endpoint structure and behavior.
- Organize Datadog monitor files by creating a subfolder for each controller inside `/datadog` (e.g., `/datadog/[ControllerName]/`). Place each controller's monitors in its respective folder.

## README Maintenance & Quick Start

- Always keep the `README.md` up to date with:
  - A brief project description and its purpose.
  - Prerequisites and setup instructions.
  - How to run the application (including a sample `curl` command for a basic endpoint, e.g., `curl http://localhost:8080/api/patients/{patientId}/summary`).
  - How to run tests.
  - A pointer to the Postman collection (`postman/HealthConnectSummary.postman_collection.json`) for comprehensive API testing.
  - References to API documentation (Swagger/OpenAPI UI), diagrams, and the Interface Control Document (ICD) in the `docs/` folder.
- Ensure new contributors can quickly get started by following the README instructions for setup, running, and testing the API.

## Postman Collection Guidelines

- When adding or updating endpoints, always ensure that all required variables (such as `baseUrl`) and any other necessary Postman-specific configuration are included in the Postman collection file (`postman/HealthConnectSummary.postman_collection.json`).
- All API endpoints must have corresponding requests in the Postman collection with necessary variables and configurations included.
- When adding or updating a controller, always:
  - Add or update the corresponding Postman request.
  - Add or update test scripts in the request (e.g., check status code, response body).
  - Commit the updated collection.
- Define collection-level variables for any values referenced in requests (e.g., `baseUrl`, authentication tokens, etc.) so the collection works out-of-the-box when imported into Postman.
- Keep the Postman collection up to date with all endpoints, variables, and test scripts to ensure smooth API testing and automation.

## 📚 References

- https://google.github.io/styleguide/javaguide.html
- https://docs.spring.io/spring-boot/index.html
