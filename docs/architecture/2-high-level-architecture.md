# 2. High Level Architecture

### 2.1. Technical Summary

The architecture for "My Groceries" is a **local-first monolithic system** designed for simplicity and user privacy, aligning with the PRD's core goals. The system is built entirely within a **Kotlin Multiplatform monorepo**, enabling maximum code sharing between the backend and the cross-platform clients (Web, Android, iOS). A local **Ktor server** will manage business logic and API endpoints, communicating with a local **PostgreSQL database** for data persistence. The UI will be delivered by **Compose for Multiplatform**. This pragmatic approach ensures rapid development for the MVP while establishing a solid foundation for future enhancements like a hosted version.

### 2.2. High Level Overview

*   **Architectural Style:** Monolith. A single, locally-run application process contains both the backend server and serves the client application. This is ideal for the MVP's local-first requirement (NFR1).
*   **Repository Structure:** Monorepo. This aligns with the standard KMP project structure and simplifies dependency management and code sharing across server and client modules (as per PRD Technical Assumptions).
*   **Primary Data Flow:** The user interacts with **shared UI components (Composables)** located in the `shared` module. UI actions are handled by shared **ViewModels**, also in the `shared` module. These ViewModels call a shared API client, which communicates with the local Ktor server via HTTP. The server processes requests and returns data. The ViewModels update the UI state, which is observed by the shared UI components for rendering. The platform-specific modules (`android`, `ios`, `web`) act as thin wrappers that host this shared UI.
*   **Key Rationale:** This architecture prioritizes user data privacy and control by keeping all components on the user's machine. The use of KMP minimizes redundant code and ensures a consistent technology stack across all layers of the application.

### 2.3. High Level Project Diagram

```mermaid
graph TD
    User[User]
    subgraph User's Local Machine
        subgraph "My Groceries Application"
            subgraph "Platform-Specific Client Shells"
                UI_Web[Web Shell (JS)]
                UI_Android[Android Shell (JVM)]
                UI_iOS[iOS Shell (Native)]
            end

            Shared_Code[Shared KMP Module <br/> (UI Components, ViewModels, Data Models, API Client)]

            subgraph "Local Server (Ktor)"
                API[REST API Endpoints] --> Logic[Business Logic/Services]
            end
            DB[(PostgreSQL Database)]
        end
    end
    
    User --> UI_Web
    User --> UI_Android
    User --> UI_iOS

    UI_Web & UI_Android & UI_iOS --> Shared_Code
    Shared_Code --> API
    Logic --> DB
```

### 2.4. Architectural and Design Patterns

*   **Repository Pattern:** Data access logic will be abstracted behind a repository layer within the server. This decouples the business logic from the database implementation, enabling easier testing (with mocks) and future flexibility if the data store were to change.
*   **Dependency Injection (DI):** A DI framework (like Koin or manual injection) will be used on the server to manage dependencies between services, repositories, and controllers. This promotes loose coupling and testability.
*   **Model-View-ViewModel (MVVM) with Shared UI:** The application will adopt the MVVM pattern. **ViewModels and UI components (Composables/Views) will be implemented in the `shared` module** to maximize code reuse. These shared ViewModels will contain the UI state and business logic, interacting with the API client to fetch data and exposing state to the shared composable UI.
*   **RESTful API:** Communication between the client and server will be via a RESTful API. This is a standard, well-understood pattern for client-server communication.