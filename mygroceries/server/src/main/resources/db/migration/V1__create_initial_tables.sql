
CREATE TABLE categories (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id UUID NOT NULL,
    CONSTRAINT fk_products_categories FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    order_date TIMESTAMPTZ NOT NULL,
    total_cost NUMERIC(10, 2) NOT NULL
);

CREATE TABLE line_items (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    cost NUMERIC(10, 2) NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_line_items_orders FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_line_items_products FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_line_items_order_id ON line_items(order_id);
CREATE INDEX idx_line_items_product_id ON line_items(product_id);
