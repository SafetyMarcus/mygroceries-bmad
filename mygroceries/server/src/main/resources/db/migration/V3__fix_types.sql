-- Drop total_cost column
ALTER TABLE orders
DROP COLUMN total_cost;

-- Change cost column type to INT
ALTER TABLE line_items
ALTER COLUMN cost TYPE INT;