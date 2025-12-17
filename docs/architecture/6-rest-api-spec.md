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
    post:
      summary: Create a new category
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/NewCategory'
      responses:
        '201':
          description: Category created successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Category'
        '400':
          description: Invalid input
    get:
      summary: Retrieve all categories
      responses:
        '200':
          description: A list of all categories
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Category'
  /categories/{id}:
    get:
      summary: Retrieve a single category by ID
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
            format: uuid
      responses:
        '200':
          description: Category found
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Category'
        '404':
          description: Category not found
    put:
      summary: Update an existing category
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
            format: uuid
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Category'
      responses:
        '200':
          description: Category updated successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Category'
        '400':
          description: Invalid input or ID mismatch
        '404':
          description: Category not found
    delete:
      summary: Delete a category
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
            format: uuid
      responses:
        '204':
          description: Category deleted successfully
        '404':
          description: Category not found
  /products:
    get:
      summary: Retrieve all products
      responses:
        '200':
          description: A list of all products
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Product'
    post:
      summary: Create a new product
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/NewProduct'
      responses:
        '201':
          description: Product created successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Product'
        '400':
          description: Invalid input or category does not exist
  /products/{id}:
    get:
      summary: Retrieve a single product by ID
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
            format: uuid
      responses:
        '200':
          description: Product found
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Product'
        '404':
          description: Product not found
    put:
      summary: Update an existing product
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
            format: uuid
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Product'
      responses:
        '200':
          description: Product updated successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Product'
        '400':
          description: Invalid input, category does not exist, or ID mismatch
        '404':
          description: Product not found
    delete:
      summary: Delete a product
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
            format: uuid
      responses:
        '204':
          description: Product deleted successfully
        '404':
          description: Product not found
components:
  schemas:
    NewCategory:
      type: object
      required:
        - name
      properties:
        name:
          type: string
    Category:
      type: object
      required:
        - id
        - name
      properties:
        id:
          type: string
          format: uuid
        name:
          type: string
    NewProduct:
      type: object
      required:
        - name
        - categoryId
      properties:
        name:
          type: string
        categoryId:
          type: string
          format: uuid
    Product:
      type: object
      required:
        - id
        - name
        - categoryId
      properties:
        id:
          type: string
          format: uuid
        name:
          type: string
        categoryId:
          type: string
          format: uuid

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