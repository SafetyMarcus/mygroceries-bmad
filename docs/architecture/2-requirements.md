# 2. Requirements

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