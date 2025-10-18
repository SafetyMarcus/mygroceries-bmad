# 6. Epic 1: Core Backend & Data Foundation

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