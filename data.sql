INSERT INTO Dish VALUES (1,'Salade fraîche','START'),(2,'Poulet grilé','MAIN'),
(3,'Riz aux légumes','MAIN'),(4,'Gâteau au chocolat','DESSERT'),(5,'SALADE de fruits','DESSERT');

INSERT INTO Ingredient VALUES (1,'Laitue',800.00,'VEGETABLE',1),(2,'Tomate',600.00,'VEGETABLE',1),
(3,'Poulet',4500.00,'ANIMAL',2),(4,'Chocolat',3000.00,'OTHER',4),(5,'Beurre',2500.00,'DAIRY',4);

UPDATE dish
SET price = 2000
WHERE name = 'Salade fraîche';

UPDATE dish
SET price = 6000
WHERE name = 'Poulet grilé';

UPDATE dish
SET price = NULL
WHERE name IN ('Riz aux légumes', 'Gâteau au chocolat', 'SALADE de fruits');







