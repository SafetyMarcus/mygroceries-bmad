# 9. Test Strategy and Standards

As per the PRD, the project will include both unit and integration tests.

*   **Unit Tests:**
    *   **Scope:** Individual functions, services, and composables in isolation.
    *   **Location:** Within each module's `commonTest` or `test` source set.
    *   **Tools:** Kotest for assertions, MockK for mocking dependencies (e.g., mocking repository layer in service tests).
    *   **Requirement:** All business logic in the `server` module and `ViewModel` logic in client modules must be covered by unit tests.

*   **Integration Tests:**
    *   **Scope:** Verify the interactions between components, primarily focusing on the API endpoints.
    *   **Location:** Within the `server` module's `test` source set.
    *   **Approach:** Tests will spin up an in-memory or Testcontainers instance of the Ktor application and make real HTTP requests to the endpoints, asserting the responses and database state changes.
    *   **Requirement:** All API endpoints defined in Epic 1 must have corresponding integration tests.