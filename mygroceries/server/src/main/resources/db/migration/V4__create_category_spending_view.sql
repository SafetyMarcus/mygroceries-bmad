CREATE VIEW category_spending AS
SELECT
    c.id AS category_id,
    c.name AS category_name,
    COALESCE(SUM(li.cost), 0) AS total_spent_cents
FROM categories c
LEFT JOIN products p ON c.id = p.category_id
LEFT JOIN line_items li ON p.id = li.product_id
GROUP BY c.id, c.name;