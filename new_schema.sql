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
ALTER TABLE Dish DROP COLUMN id_dih;



UPDATE Dish SET selling_price = 3500.00 WHERE id_dish = 1;
UPDATE Dish SET selling_price = 12000.00 WHERE id_dish = 2;
UPDATE Dish SET selling_price = NULL WHERE id_dish = 3;
UPDATE Dish SET selling_price = 8000.00 WHERE id_dish = 4;
UPDATE Dish SET selling_price = NULL WHERE id_dish = 5;

CREATE TYPE mouvement_type AS ENUM ('IN','OUT');

CREATE TABLE StockMovement(
id_stockMovement SERIAL PRIMARY KEY,
id_ingredient int,
quantity NUMERIC,
type mouvement_type,
unit unit_type,
creation_datetime timestamp
);


INSERT INTO StockMovement VALUES (1, 1, 5.0, 'IN', 'KG','2024-01-05 08:00'),(2, 1, 0.2, 'OUT', 'KG', '2024-01-06 12:00'),(3, 2, 4.0, 'IN', 'KG', '2024-01-05 08:00'),(4, 2, 0.15, 'OUT', 'KG', '2024-01-06 12:00'),
(5, 3, 10.0, 'IN', 'KG', '2024-01-04 09:00'),(6, 3, 1.0, 'OUT','KG', '2024-01-06 13:00'),(7, 4, 3.0, 'IN', 'KG', '2024-01-05 10:00'),(8 ,4 ,0.3 ,'OUT','KG','2024-01-06 14:00'),(9 ,5 ,2.5 , 'IN','KG', '2024-01-05 10:00')
,(10 ,5 ,0.2 ,'OUT','KG' ,'2024-01-06 14:00');


CREATE TABLE "Order"(
    id_order SERIAL PRIMARY KEY,
    reference varchar(100),
    creation_datetime timestamp,
    id_table int,
    heure_entrer timestamp,
    heure_sortie timestamp,
    FOREIGN KEY (id_table) REFERENCES "table"(id_space)


);

CREATE TABLE DishOrder(
    int SERIAL PRIMARY KEY,
    id_order int NOT NULL  ,
    FOREIGN KEY (id_order) references "Order"(id_order),
    id_dish int not null,
    FOREIGN KEY (id_dish) references Dish(id_dish),
    quantity int 
      
);
CREATE TABLE "table"(
    id_space SERIAL PRIMARY KEY,
    table_numero int not null unique

);

INSERT INTO DishOrder (id_order, id_dish, quantity) VALUES
(1, 1, 2),  
(1, 2, 1),  
(2, 1, 1); 






