# My Groceries Product Requirements Document (PRD)

## 1. Goals and Background Context

### Goals

*   Develop a functional data visualization tool for personal grocery spending analysis.
*   Enable users to import their own data to identify key spending habits and categories.
*   Empower users with personalized financial insights through their own data.
*   Achieve rapid insight into top spending categories (less than 1 minute).
*   Ensure a quick and easy data import process (less than 5 minutes).

### Background Context

With the rising cost of living, individuals are finding it increasingly difficult to manage their grocery expenses. This is largely due to inconsistent and non-granular data from major retailers, which makes it challenging to track spending effectively. "My Groceries" aims to solve this by providing a local-first data visualization tool that empowers users to analyze their own spending data, identify savings opportunities, and gain control over their grocery budgets.

### Change Log

| Date | Version | Description | Author |
| :--- | :--- | :--- | :--- |
| 2025-10-18 | 1.0 | Initial draft of PRD from Project Brief. | John (PM) |

## 2. Requirements

### Functional Requirements

1.  **FR1:** The system shall ingest structured JSON data for **Orders** via a server API. An **Order** contains a date, total cost, and a list of **LineItems**.
2.  **FR2:** A **LineItem** links a **Product** with its cost and quantity for that order.
3.  **FR3:** A **Product** has a name and belongs to a **Category**.
4.  **FR4:** The main dashboard must display a graph of individual **Order** totals over time.
5.  **FR5:** The main dashboard must display a list of all **Categories**, ranked by their total spend.
6.  **FR6:** When a user selects a **Category**, the application will navigate to a "Category Drill-Down View".
7.  **FR7:** The Category Drill-Down View will display a graph showing the total spend for that **Category** over time.
8.  **FR8:** The Category Drill-Down View will display a list of all **Products** within that category, ranked by total spend.
9.  **FR9:** When a user selects a **Product**, the application will navigate to a read-only "Product Drill-Down View".
10. **FR10:** The Product Drill-Down View will display a graph showing the cost of that **Product** in each order over time.
11. **FR11:** The Product Drill-Down View will display a list of all **Orders** in which the product was purchased.

### Non-Functional Requirements

1.  **NFR1:** The application must run locally on the user's machine, with the backend server as part of the same application.
2.  **NFR2:** The entire project will be developed in a single Kotlin Multiplatform codebase.
3.  **NFR3:** The UI for all platforms (Web, Android, iOS) will be built with Compose for Multiplatform.
4.  **NFR4:** The UI will adhere to a modern Material Design aesthetic.
5.  **NFR5:** Data will be stored in a local PostgreSQL database.
6.  **NFR6:** The application must feel responsive and performant on all target platforms.
7.  **NFR7:** For the MVP, the application will not support user accounts, login, or multi-user functionality.
8.  **NFR8:** The client application will not support data modification (CRUD operations). However, the local server must provide API endpoints for full CRUD (Create, Read, Update, Delete) operations on all data models.
9.  **NFR9:** For the MVP, the client application will not include a user-facing data import feature. Data will be populated via direct database interaction or server-side processes.

## 3. User Interface Design Goals

### Overall UX Vision

The user experience should be clean, intuitive, and empowering. The primary goal is to provide users with maximum clarity on their spending with minimum effort. The interface will prioritize data visualization, presenting insights in a way that is easy to understand at a glance.

### Key Interaction Paradigms

*   **Drill-Down Navigation:** The core user journey will be a seamless drill-down from the main dashboard (high-level overview) into specific categories and then individual products (granular detail).
*   **Interactive Visualizations:** Users will interact primarily with charts and sorted lists to explore their data. Selections on these components will trigger navigation to more detailed views.

### Core Screens and Views

*   **Main Dashboard:** The central view showing order history and top spending categories.
*   **Category Drill-Down View:** A detailed view for a single category, showing its spending trend and a ranked list of products within it.
*   **Product Drill-Down View:** A read-only view showing the purchase history and cost trend for a single product.

### Accessibility

*   **Target:** WCAG 2.1 AA

### Branding

*   The application will adhere to a modern **Material Design** aesthetic, using standard components and design principles.

### Target Device and Platforms

*   **Platforms:** Cross-Platform (Web, Android, iOS)
*   The application will be built from a single codebase to target modern evergreen web browsers as well as the last two major OS versions for Android and iOS.

## 4. Technical Assumptions

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

## 5. Epic List

1.  **Epic 1: Core Backend & Data Foundation:** Establish the foundational Kotlin Multiplatform project structure, set up the local PostgreSQL database, and implement the core data models and server-side CRUD APIs.
2.  **Epic 2: Main Dashboard Visualization:** Develop the main dashboard UI to fetch and display the graph of order totals and the ranked list of spending categories.
3.  **Epic 3: Detailed Spending Drill-Down Views:** Implement the category and product drill-down screens, allowing users to navigate from the main dashboard to view detailed spending trends.

## 6. Epic 1: Core Backend & Data Foundation

**Epic Goal:** This epic lays the groundwork for the entire application. It involves setting up the Kotlin Multiplatform project with all necessary dependencies, defining the database schema, and creating the server-side API endpoints to manage the core data entities. The successful completion of this epic will result in a functional local server with a fully operational API, ready for the front-end to be built upon it.

---

### Story 1.1: Project Scaffolding & Dependencies

**As a** developer,
**I want** a properly configured Kotlin Multiplatform project structure,
**so that** I can efficiently build and share code between the server, web, Android, and iOS clients.

#### Acceptance Criteria

1.  A new Kotlin Multiplatform project is created with a monorepo structure.
2.  The project includes modules for `shared`, `server`, `web`, `android`, and `ios`.
3.  Core dependencies are added and resolved: Ktor (server/client), Compose Multiplatform (UI), PostgreSQL driver (server), and Kotlinx Serialization (JSON).
4.  A basic health-check endpoint is functional on the Ktor server to verify the setup.
5.  The project can be successfully built for all target platforms (server, web, android, ios).

---

### Story 1.2: Database Schema and Connection

**As a** developer,
**I want** the database schema to be defined and a connection established from the server,
**so that** the application can persist and retrieve data.

#### Acceptance Criteria

1.  Database migration files are created to define the tables for `categories`, `products`, `orders`, and `line_items`.
2.  The table schemas correctly reflect the required data models and their relationships (e.g., a `products` table with a foreign key to the `categories` table).
3.  The Ktor server is configured to connect to the local PostgreSQL database on startup.
4.  The server can apply the migrations to the database to create the schema.
5.  A basic connection pooling mechanism is configured.

---

### Story 1.3 (v1.1): Implement Category CRUD API

**As a** developer,
**I want** to create API endpoints for managing `Category` data,
**so that** categories can be created, retrieved, updated, and deleted from the database.

#### Acceptance Criteria

1.  A `Category` data class is defined in the `shared` module to be used by both client and server.
2.  The Ktor server exposes RESTful endpoints at a `/categories` path.
3.  The following endpoints are implemented:
    *   `POST /categories`: Creates a new category.
    *   `GET /categories`: Retrieves all categories.
    *   `GET /categories/{id}`: Retrieves a single category by its ID.
    *   `PUT /categories/{id}`: Updates an existing category.
    *   `DELETE /categories/{id}`: Deletes a category.
4.  Each endpoint correctly performs the corresponding Create, Read, Update, or Delete operation in the PostgreSQL database.
5.  Server-side validation is implemented:
    *   All category IDs must be handled as UUIDs.
    *   The `name` for a category cannot be empty.
6.  The functionality is covered by automated tests.

---

### Story 1.4: Implement Product CRUD API

**As a** developer,
**I want** to create API endpoints for managing `Product` data,
**so that** products can be created, retrieved, updated, and deleted from the database.

#### Acceptance Criteria

1.  A `Product` data class is defined in the `shared` module. It must include a field for its associated `categoryId`.
2.  The Ktor server exposes RESTful endpoints at a `/products` path.
3.  The following endpoints are implemented:
    *   `POST /products`: Creates a new product.
    *   `GET /products`: Retrieves all products.
    *   `GET /products/{id}`: Retrieves a single product by its ID.
    *   `PUT /products/{id}`: Updates an existing product.
    *   `DELETE /products/{id}`: Deletes a product.
4.  Each endpoint correctly performs the corresponding Create, Read, Update, or Delete operation in the PostgreSQL database.
5.  Server-side validation is implemented:
    *   All product IDs must be handled as UUIDs.
    *   The `name` for a product cannot be empty.
    *   The `categoryId` provided when creating or updating a product must correspond to an existing category in the database.
6.  The functionality is covered by automated tests.

---

### Story 1.5: Implement Order and LineItem CRUD API

**As a** developer,
**I want** to create API endpoints for managing `Order` and `LineItem` data,
**so that** a user's shopping orders can be recorded and managed in the system.

#### Acceptance Criteria

1.  `Order` and `LineItem` data classes are defined in the `shared` module.
    *   An `Order` includes a date, a total cost, and a list of `LineItems`.
    *   A `LineItem` includes a `productId`, quantity, and the cost for that item in the order.
2.  The Ktor server exposes RESTful endpoints at an `/orders` path.
3.  The following endpoints are implemented:
    *   `POST /orders`: Creates a new order and its associated line items in a single transaction.
    *   `GET /orders`: Retrieves all orders.
    *   `GET /orders/{id}`: Retrieves a single order, including its full list of line items.
    *   `PUT /orders/{id}`: Updates an existing order's top-level details (e.g., the order date).
    *   `DELETE /orders/{id}`: Deletes an order and all of its associated line items.
4.  Server-side validation is implemented:
    *   All `Order` IDs must be handled as UUIDs.
    *   An `Order` must have a valid date and a non-negative total cost.
    *   For every `LineItem` in a request, the `productId` must correspond to an existing product, and the `quantity` and `cost` must be non-negative.
5.  The functionality is covered by automated tests.

## 7. Epic 2: Main Dashboard Visualization

**Epic Goal:** This epic focuses on building the primary user-facing view of the application: the main dashboard. It will consume the APIs created in Epic 1 to display high-level spending insights. The goal is to provide users with an immediate, at-a-glance understanding of their overall spending patterns.

---

### Story 2.1: Basic UI Shell and Navigation

**As a** user,
**I want** a basic application window with a clear title and structure,
**so that** I have a consistent and recognizable frame for all application content.

#### Acceptance Criteria

1.  A main application view is created using Compose for Multiplatform that serves as the container for all other UI content.
2.  The view includes a top app bar displaying the application title, "My Groceries".
3.  A basic navigation framework is put in place to handle moving between different screens (even though only the dashboard will exist at first).
4.  The basic UI shell renders correctly and looks consistent on all target platforms (web, Android, and iOS).
5.  The shell adheres to the fundamental principles of Material Design.

---

### Story 2.2 (new): Material Theme and Color Scheme

**As a** user,
**I want** the application to have a visually appealing and consistent color scheme,
**so that** the interface is pleasant to use and easy to read.

#### Acceptance Criteria

1.  A Material 3 color scheme is generated, defining primary, secondary, surface, and background colors.
2.  The color scheme is implemented as a custom `MaterialTheme` in the `shared` Compose for Multiplatform code.
3.  The custom theme is applied to the UI shell from the previous story, replacing the default theme.
4.  The theme must provide definitions for both a light and a dark color mode.
5.  The chosen color combinations meet WCAG AA contrast ratio guidelines for readability.

---

### Story 2.3: API Client Implementation

**As a** developer,
**I want** a client-side API service that can communicate with the backend,
**so that** the UI modules can fetch the data they need to display.

#### Acceptance Criteria

1.  A Ktor-based HTTP client is configured within the `shared` module.
2.  Client-side functions are implemented to call all the `GET` endpoints defined in Epic 1 (e.g., `GET /orders`, `GET /categories`, `GET /products`).
3.  The client correctly deserializes the JSON responses from the server into the shared data classes.
4.  Basic error handling is implemented to manage network issues or unsuccessful API responses.
5.  The API client is usable from all target platforms (web, Android, iOS).

---

### Story 2.4: Display Order Totals Graph

**As a** user,
**I want** to see a graph of my individual order totals over time on the main dashboard,
**so that** I can quickly visualize my spending per shopping trip.

#### Acceptance Criteria

1.  A new Composable is created for the main dashboard screen.
2.  The dashboard's view model (or equivalent logic) uses the API client to fetch the list of all orders.
3.  A chart component (e.g., a line or bar chart) is added to the dashboard UI.
4.  The chart plots the total cost of each order against its date, providing a clear historical view of spending.
5.  A loading indicator is displayed while the order data is being fetched.
6.  A user-friendly error message is displayed on the UI if the data fails to load.
7.  The chart and any surrounding UI elements are styled using the custom `MaterialTheme`.

---

### Story 2.5 (v2): Enhance Category API with Sorting

**As a** developer,
**I want** to enhance the `GET /categories` API endpoint to support sorting by total spend,
**so that** the client can request a ranked list of categories in a consistent, RESTful way.

#### Acceptance Criteria

1.  The existing `GET /categories` endpoint is modified to accept an optional `sortBy` query parameter (e.g., `sortBy=spend:desc`).
2.  When `sortBy=spend:desc` is provided, the server calculates the total spend for each category.
3.  The endpoint returns a list of category objects, sorted in descending order by their total spend. The total spend amount should be included in the data for each category.
4.  The backend logic is optimized to handle this calculation and sorting efficiently.
5.  Automated tests are updated to verify the new sorting capability.

---

### Story 2.6: Display Ranked Category List

**As a** user,
**I want** to see a list of my grocery categories ranked by total spending,
**so that** I can immediately identify where most of my money is going.

#### Acceptance Criteria

1.  The dashboard's view model (or equivalent logic) uses the API client to call the new `/analytics/categories/ranked` endpoint.
2.  A list component is added to the dashboard UI.
3.  Each item in the list displays the category's name and the total spend amount, using the data directly from the API response.
4.  The list is rendered in the pre-sorted order received from the server.
5.  Each item in the list is a selectable element (e.g., a button or clickable row) to prepare for the drill-down functionality in the next epic.
6.  A loading indicator is shown while the data is being fetched.
7.  The list and its items are styled using the custom `MaterialTheme`.

## 8. Epic 3: Detailed Spending Drill-Down Views

**Epic Goal:** This epic completes the core user journey by building the drill-down views. It allows users to navigate from the high-level dashboard into specific categories and products to analyze their spending in detail. The goal is to provide users with the tools to understand the "why" behind their top-level spending numbers.

---

### Story 3.1 (v2): Enhance Product API with Filtering and Sorting

**As a** developer,
**I want** to enhance the `GET /products` API endpoint to support filtering by category and sorting by total spend,
**so that** clients can request ranked lists of products without needing a separate analytics endpoint.

#### Acceptance Criteria

1.  The existing `GET /products` endpoint is modified to accept an optional `categoryId` query parameter. When provided, it returns only products belonging to that category.
2.  The endpoint is also modified to accept an optional `sortBy` query parameter (e.g., `sortBy=spend:desc`).
3.  When `sortBy=spend:desc` is used, the server calculates the total spend for each product and sorts the results in descending order before returning them.
4.  The endpoint can handle both parameters at once, allowing a request like `GET /products?categoryId={id}&sortBy=spend:desc`.
5.  The backend logic is optimized to handle this filtering and sorting efficiently at the database level.
6.  Automated tests are updated to verify the new filtering and sorting capabilities.

---

### Story 3.2: Enhance Orders API with Filtering

**As a** developer,
**I want** to enhance the `GET /orders` API endpoint to support filtering by category,
**so that** the client can efficiently fetch the data needed to render category-specific spending graphs.

#### Acceptance Criteria

1.  The existing `GET /orders` endpoint is modified to accept an optional `categoryId` query parameter.
2.  When the `categoryId` parameter is provided, the endpoint returns only the orders that contain at least one product from that category.
3.  To minimize the data sent, the `lineItems` in the returned orders should only contain items relevant to the specified category.
4.  The backend logic is optimized to perform this filtering efficiently.
5.  Automated tests are updated to verify the new filtering capability.

---

### Story 3.3: Implement Category Drill-Down View

**As a** user,
**I want** to select a category from the dashboard and see a detailed view of my spending within that category,
**so that** I can understand my spending habits for a specific group of products.

#### Acceptance Criteria

1.  Selecting a category from the dashboard navigates the user to a new "Category Drill-Down" screen, passing the category's ID.
2.  The screen's top app bar displays the name of the selected category.
3.  The view model makes two API calls:
    *   It calls `GET /orders?categoryId={id}` to get the data for the spending graph.
    *   It calls `GET /products?categoryId={id}&sortBy=spend:desc` to get the ranked list of products.
4.  A graph displays the total spend for that category over time.
5.  A list displays the ranked products from the API, showing each product's name and its total spend.
6.  Each product in the list is a selectable element.
7.  Loading indicators are shown while the API calls are in progress.
8.  The view is styled using the custom `MaterialTheme`.

---

### Story 3.4: Enhance Orders API with Product Filtering

**As a** developer,
**I want** to enhance the `GET /orders` API endpoint to support filtering by product ID,
**so that** the client can efficiently fetch the complete purchase history for a single product.

#### Acceptance Criteria

1.  The existing `GET /orders` endpoint is modified to accept an optional `productId` query parameter.
2.  When the `productId` parameter is provided, the endpoint returns only the orders that contain a line item for that specific product.
3.  The backend logic is optimized to perform this filtering efficiently.
4.  Automated tests are updated to verify the new filtering capability.

---

### Story 3.5: Implement Product Drill-Down View

**As a** user,
**I want** to select a product and see its purchase history,
**so that** I can understand how my spending on a specific item has changed over time.

#### Acceptance Criteria

1.  Selecting a product from the Category Drill-Down screen navigates the user to a new, read-only "Product Drill-Down" screen, passing the product's ID.
2.  The screen's top app bar displays the name of the selected product.
3.  The view model calls the `GET /orders?productId={id}` endpoint to fetch the product's purchase history.
4.  The view contains a graph showing the cost of that individual product over time.
5.  Below the graph, a list displays all the orders in which the product was purchased, showing details like the order date, quantity, and cost in that order.
6.  A loading indicator is shown while data is being fetched.
7.  The entire view is styled using the custom `MaterialTheme`.

## 9. Checklist Results Report

### Executive Summary

*   **Overall PRD Completeness:** 90%
*   **MVP Scope Appropriateness:** Just Right
*   **Readiness for Architecture Phase:** Ready
*   **Most Critical Gaps:** Minor, non-blocking gaps in non-functional requirements (e.g., security hardening, specific operational needs), which are acceptable for an initial MVP of this nature.

### Category Analysis Table

| Category | Status | Critical Issues |
| :--- | :--- | :--- |
| 1. Problem Definition & Context | PASS | None |
| 2. MVP Scope Definition | PASS | None |
| 3. User Experience Requirements | PASS | None |
| 4. Functional Requirements | PASS | None |
| 5. Non-Functional Requirements | PARTIAL| Security, compliance, and detailed reliability requirements are not explicitly defined. |
| 6. Epic & Story Structure | PASS | None |
| 7. Technical Guidance | PASS | None |
| 8. Cross-Functional Requirements | PARTIAL| Operational requirements (monitoring, deployment specifics) are not explicitly defined. |
| 9. Clarity & Communication | PASS | None |

### Top Issues by Priority

*   **MEDIUM:** It would be beneficial to add placeholder sections for **Security** and **Reliability** in the Non-Functional Requirements to be addressed post-MVP.
*   **LOW:** Explicit **Operational Requirements** (like monitoring or specific deployment steps) can be deferred until the project moves towards a hosted or more widely distributed version.

### MVP Scope Assessment

The MVP scope is well-defined and appropriate. It focuses on the core user journey of data import and analysis, correctly deferring features like user accounts and in-app data editing.

### Technical Readiness

The PRD provides clear technical constraints and a solid architectural foundation. The identified risk of PostgreSQL setup complexity for end-users is noted correctly and is a key consideration for the architect.

### Recommendations

1.  **Proceed to Architecture:** The document is ready for an architect to begin designing the system.
2.  **Acknowledge NFR Gaps:** While not blockers for the MVP, the team should be aware of the non-functional requirements that will need to be specified in future releases (primarily around security and operations).

### Final Decision

*   **READY FOR ARCHITECT**: The PRD and epics are comprehensive, properly structured, and ready for architectural design.

## 10. Next Steps

### UX Expert Prompt

Please review the attached PRD for the 'My Groceries' application, paying close attention to the 'User Interface Design Goals' section. Based on this document, please develop a front-end architecture and specification. The core technologies are Kotlin Multiplatform with Compose for Multiplatform, and the UI should adhere to Material Design principles.

### Architect Prompt

Please review the attached PRD for the 'My Groceries' application. Your task is to create a comprehensive system architecture based on the requirements, technical assumptions, and detailed user stories. The core technology stack is Kotlin Multiplatform, Ktor, and PostgreSQL. Please ensure your architecture addresses all functional and non-functional requirements for the MVP.