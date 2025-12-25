
CREATE TYPE D AS ENUM ('START','MAIN','DESSERT');
CREATE TABLE Dish(
    id_dish int primary key,
    name varchar(100),
    dish_type D
);



CREATE TYPE C AS ENUM ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
CREATE TABLE Ingredient(
    id_ingredient int primary key,
    name varchar(100),
    price float,    
    category C,
    dish_id int,
    FOREIGN KEY (dish_id) REFERENCES Dish(id_dish) 

);





