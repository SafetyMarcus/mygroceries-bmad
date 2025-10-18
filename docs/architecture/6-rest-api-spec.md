# 6. REST API Spec

The following OpenAPI 3.0 specification outlines the API contract required by the PRD. This will be implemented in the `server` module.

```yaml
openapi: 3.0.3
info:
  title: My Groceries API
  version: 1.0.0
  description: API for managing grocery spending data.
servers:
  - url: http://localhost:8080
    description: Local Development Server
paths:
  /categories:
    get:
      summary: Retrieve all categories, with optional sorting by spend.
      parameters:
        - name: sortBy
          in: query
          schema:
            type: string
            example: "spend:desc"
      responses:
        '200':
          description: A list of categories.
    post:
      summary: Create a new category.
      responses:
        '201':
          description: Category created.
  /categories/{id}:
    get:
      summary: Retrieve a single category by ID.
    put:
      summary: Update an existing category.
    delete:
      summary: Delete a category.
  /products:
    get:
      summary: Retrieve products, with optional filtering and sorting.
      parameters:
        - name: categoryId
          in: query
          schema:
            type: string
            format: uuid
        - name: sortBy
          in: query
          schema:
            type: string
            example: "spend:desc"
      responses:
        '200':
          description: A list of products.
    post:
      summary: Create a new product.
  /products/{id}:
    get:
      summary: Retrieve a single product by ID.
    put:
      summary: Update an existing product.
    delete:
      summary: Delete a product.
  /orders:
    get:
      summary: Retrieve orders, with optional filtering.
      parameters:
        - name: categoryId
          in: query
          schema:
            type: string
            format: uuid
        - name: productId
          in: query
          schema:
            type: string
            format: uuid
      responses:
        '200':
          description: A list of orders.
    post:
      summary: Create a new order with its line items.
  /orders/{id}:
    get:
      summary: Retrieve a single order by ID.
    put:
      summary: Update an order's top-level details.
    delete:
      summary: Delete an order and its line items.
  /health:
    get:
      summary: Health check endpoint.
      responses:
        '200':
          description: Server is healthy.
```