CREATE USER mini_dish_db_manage WITH PASSWORD "*-*";
CREATE DATABASE  mini_dish_db OWNER mini_dish_db_manage;
\C mini_dish_db

GRANT CONNECT ON DATABASE mini_dish_db TO mini_dish_db_manage;
GRANT USAGE ON SCHEMA public TO mini_dish_db_manage;
GRANT INSERT ON TABLE Dish,Ingredient TO mini_dish_db_manage;
GRANT SELECT ON TABLE Dish,Ingredient TO mini_dish_db_manage;
GRANT UPDATE ON TABLE Dish,Ingredient TO mini_dish_db_manage;
GRANT DELETE ON TABLE Dish,Ingredient TO mini_dish_db_manage;

REVOKE CREATE ON SCHEMA public FROM mini_dish_db;

