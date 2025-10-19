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
