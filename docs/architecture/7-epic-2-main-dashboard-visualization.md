# 7. Epic 2: Main Dashboard Visualization

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