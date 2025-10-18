# 3. Tech Stack

### 3.1. Cloud Infrastructure

*   **Provider:** N/A for MVP. The application is local-first.
*   **Key Services:** N/A
*   **Deployment Regions:** N/A

### 3.2. Technology Stack Table

| Category | Technology | Version | Purpose | Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **Language** | Kotlin | 1.9.23 | Primary language for all code | Required for KMP, modern, and type-safe. |
| **Core Framework** | Kotlin Multiplatform | 1.6.10 | Enables code sharing | A core non-functional requirement (NFR2). |
| **UI Framework** | Compose Multiplatform | 1.6.1 | UI for Web, Android, iOS | A core non-functional requirement (NFR3). |
| **Backend Framework** | Ktor | 2.3.9 | Local server and client HTTP | Lightweight, idiomatic Kotlin, and well-integrated with KMP. |
| **Database** | PostgreSQL | 15.x | Local data persistence | A core non-functional requirement (NFR5). |
| **Database Driver** | Exposed | 0.49.0 | SQL framework for Kotlin | Provides a type-safe DSL for interacting with PostgreSQL. |
| **Serialization** | Kotlinx Serialization | 1.6.3 | JSON handling | Native Kotlin library for serializing data models for the API. |
| **Testing** | Kotest | 5.8.1 | Unit and integration testing | Provides a flexible and powerful testing framework for Kotlin. |
| **Testing** | MockK | 1.13.10 | Mocking library for tests | Simplifies mocking dependencies in unit tests. |