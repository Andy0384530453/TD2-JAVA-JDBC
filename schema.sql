
CREATE TYPE D AS ENUM ('START','MAIN','DESSERT');
CREATE TABLE Dish(
    id_dish int primary key,
    name varchar(100),
    dish_type D
);
ALTER TABLE Dish
ADD COLUMN IF NOT EXISTS price Numeric NULL;

ALTER TABLE Dish OWNER TO mini_dish_db_manage;



CREATE TYPE C AS ENUM ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
CREATE TABLE Ingredient(
    id_ingredient int primary key,
    name varchar(100),
    price float,    
    category C

);

CREATE TYPE unit_type AS ENUM ('KG','L');
CREATE TABLE DishIngredient(
    id_dishIngredient SERIAL primary key,
    id_dish int,
    id_ingredient int,
    quantity_required NUMERIC,
    Unit unit_type
);






