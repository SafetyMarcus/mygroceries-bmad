# 4. Data Models

The following conceptual models are derived from the PRD (FR1, FR2, FR3). They will be implemented as `@Serializable` data classes in the `shared` module.

### 4.1. Category

*   **Purpose:** Represents a category for grocery products (e.g., "Dairy", "Produce").
*   **Key Attributes:**
    *   `id`: UUID - Unique identifier.
    *   `name`: String - The name of the category.
*   **Relationships:**
    *   One-to-Many: A `Category` can have many `Products`.

### 4.2. Product

*   **Purpose:** Represents a specific grocery product (e.g., "Milk", "Apples").
*   **Key Attributes:**
    *   `id`: UUID - Unique identifier.
    *   `name`: String - The name of the product.
    *   `categoryId`: UUID - Foreign key linking to the `Category`.
*   **Relationships:**
    *   Many-to-One: A `Product` belongs to one `Category`.
    *   Many-to-Many: A `Product` can be in many `Orders` (via `LineItem`).

### 4.3. Order

*   **Purpose:** Represents a single shopping trip or transaction.
*   **Key Attributes:**
    *   `id`: UUID - Unique identifier.
    *   `date`: Instant - The date and time of the order.
    *   `totalCost`: BigDecimal - The total cost of the order.
*   **Relationships:**
    *   One-to-Many: An `Order` can have many `LineItems`.

### 4.4. LineItem

*   **Purpose:** A join-table entity linking a `Product` to an `Order`, representing a single item purchased in that order.
*   **Key Attributes:**
    *   `id`: UUID - Unique identifier.
    *   `orderId`: UUID - Foreign key linking to the `Order`.
    *   `productId`: UUID - Foreign key linking to the `Product`.
    *   `cost`: BigDecimal - The cost of this item in this specific order.
    *   `quantity`: Double - The quantity of the product purchased.
*   **Relationships:**
    *   Many-to-One: A `LineItem` belongs to one `Order`.
    *   Many-to-One: A `LineItem` refers to one `Product`.