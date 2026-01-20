CREATE TYPE unit_type AS ENUM ('KG','L');
CREATE TABLE DishIngredient(
    id_dishIngredient SERIAL primary key,
    id_dish int,
    id_ingredient int,
    quantity_required NUMERIC,
    Unit unit_type
);
INSERT INTO DishIngredient VALUES (1, 1, 1, 0.20,'KG'),(2,1,2,0.15,'KG'),(3,2,3,1.00,'KG'),
(4,4,4,0.30,'KG'),(5,4,5,0.20,'KG');

ALTER TABLE dish RENAME COLUMN price TO selling_price;


UPDATE Dish SET selling_price = 3500.00 WHERE id_dish = 1;
UPDATE Dish SET selling_price = 12000.00 WHERE id_dish = 2;
UPDATE Dish SET selling_price = NULL WHERE id_dish = 3;
UPDATE Dish SET selling_price = 8000.00 WHERE id_dish = 4;
UPDATE Dish SET selling_price = NULL WHERE id_dish = 5;







