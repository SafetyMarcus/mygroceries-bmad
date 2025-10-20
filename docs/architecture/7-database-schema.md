# 7. Database Schema

The following SQL DDL defines the schema for the PostgreSQL database. This will be managed via migration files within the `server` module (Story 1.2).

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    category_id UUID NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_date TIMESTAMPTZ NOT NULL,
    total_cost NUMERIC(10, 2) NOT NULL
);

CREATE TABLE line_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    cost NUMERIC(10, 2) NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_line_items_order_id ON line_items(order_id);
CREATE INDEX idx_line_items_product_id ON line_items(product_id);
```

## Database Provisioning

To set up the PostgreSQL database:

1.  **Install PostgreSQL**: Ensure PostgreSQL is installed and running on your system. You can download it from [postgresql.org](https://www.postgresql.org/download/).
2.  **Create Database**: Create a new database for the application. For example, if your application expects a database named `mygroceries_db`, you can create it using the `psql` command-line tool:
    ```bash
    psql -U postgres -c "CREATE DATABASE mygroceries_db;"
    ```
    (Replace `postgres` with your PostgreSQL superuser if different).
3.  **Configure Credentials**: Ensure your application's environment variables (`DB_JDBC_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_JDBC_URL_WITH_DB`) are correctly set to connect to this database. These
    should be set in a mygroceries/local.properties file.