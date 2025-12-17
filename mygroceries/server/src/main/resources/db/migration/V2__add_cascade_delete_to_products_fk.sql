-- Drop the existing foreign key constraint
ALTER TABLE products 
DROP CONSTRAINT fk_products_categories;

-- Recreate the foreign key with ON DELETE CASCADE
ALTER TABLE products 
ADD CONSTRAINT fk_products_categories 
FOREIGN KEY (category_id) 
REFERENCES categories(id) 
ON DELETE CASCADE;
