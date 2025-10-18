# 4. Technical Assumptions

### Repository Structure: Monorepo

*   **Rationale:** The project will be housed in a single monorepo. As specified in the brief, this aligns with the standard Kotlin Multiplatform project structure and simplifies dependency management and code sharing between the client and server.

### Service Architecture: Monolith

*   **Rationale:** For the MVP, the architecture will be a monolith. The backend server logic will be part of the same Kotlin Multiplatform project and will run locally on the user's machine. This is the simplest and fastest approach for the initial local-first version of the application.

### Testing Requirements: Unit + Integration Testing

*   **Recommendation:** Committing to both **Unit Testing** for individual components and **Integration Testing** for verifying the interactions between the client, the Ktor server, and the PostgreSQL database.

### Additional Technical Assumptions and Requests

*   **Core Technology Stack:** The project will be built exclusively with **Kotlin Multiplatform**, using **Compose for Multiplatform** for the UI and **Ktor** for client-server communication.
*   **Database:** The local data store will be **PostgreSQL**.
*   **External Data Pre-processing:** It is a critical assumption that an external process will be responsible for cleaning, categorizing, and transforming raw grocery data into the required JSON format.
*   **Database Setup:** The potential complexity of requiring users to set up a local PostgreSQL instance is a known risk.