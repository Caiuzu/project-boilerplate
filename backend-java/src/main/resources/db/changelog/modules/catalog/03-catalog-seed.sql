--liquibase formatted sql

--changeset catalog:03-catalog-seed runAlways:true onError:MARK_RAN

-- Catalogos
INSERT INTO catalog (name)
SELECT 'Alimentos'
WHERE NOT EXISTS (SELECT 1 FROM catalog WHERE name = 'Alimentos');

INSERT INTO catalog (name)
SELECT 'Bebidas e Sobremesas'
WHERE NOT EXISTS (SELECT 1 FROM catalog WHERE name = 'Bebidas e Sobremesas');

INSERT INTO catalog (name)
SELECT 'Promoções'
WHERE NOT EXISTS (SELECT 1 FROM catalog WHERE name = 'Promoções');

INSERT INTO catalog (name)
SELECT 'Infantil'
WHERE NOT EXISTS (SELECT 1 FROM catalog WHERE name = 'Infantil');

-- Categorias
INSERT INTO categories (name, description, display_order, active, updated_at, catalog_id)
SELECT 'Lanches', 'Hamburgueres e sanduíches', 1, true, NOW(),
       (SELECT id FROM catalog WHERE name = 'Alimentos')
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Lanches');

INSERT INTO categories (name, description, display_order, active, updated_at, catalog_id)
SELECT 'Acompanhamentos', 'Batatas fritas, nuggets e outros acompanhamentos', 2, true, NOW(),
       (SELECT id FROM catalog WHERE name = 'Alimentos')
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Acompanhamentos');

INSERT INTO categories (name, description, display_order, active, updated_at, catalog_id)
SELECT 'Bebidas', 'Refrigerantes, sucos e outras bebidas', 3, true, NOW(),
       (SELECT id FROM catalog WHERE name = 'Bebidas e Sobremesas')
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Bebidas');

INSERT INTO categories (name, description, display_order, active, updated_at, catalog_id)
SELECT 'Sobremesas', 'Sorvetes, tortas e outras sobremesas', 4, true, NOW(),
       (SELECT id FROM catalog WHERE name = 'Bebidas e Sobremesas')
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Sobremesas');

-- Lanches
INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'X-Burger', 'Hambúrguer, queijo, alface, tomate, cebola e molho especial', 22.90,
       (SELECT id FROM categories WHERE name = 'Lanches'), 1, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'X-Burger');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'X-Bacon', 'Hambúrguer, queijo, bacon, alface, tomate e molho especial', 24.90,
       (SELECT id FROM categories WHERE name = 'Lanches'), 2, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'X-Bacon');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'X-Salada', 'Hambúrguer, queijo, alface, tomate, cebola, picles e molho especial', 20.90,
       (SELECT id FROM categories WHERE name = 'Lanches'), 3, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'X-Salada');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'X-Tudo', 'Hambúrguer, queijo, bacon, ovo, alface, tomate, cebola e molho especial', 28.90,
       (SELECT id FROM categories WHERE name = 'Lanches'), 4, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'X-Tudo');

-- Acompanhamentos
INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Batata Frita P', 'Porção pequena de batata frita', 9.90,
       (SELECT id FROM categories WHERE name = 'Acompanhamentos'), 1, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Batata Frita P');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Batata Frita M', 'Porção média de batata frita', 12.90,
       (SELECT id FROM categories WHERE name = 'Acompanhamentos'), 2, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Batata Frita M');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Batata Frita G', 'Porção grande de batata frita', 15.90,
       (SELECT id FROM categories WHERE name = 'Acompanhamentos'), 3, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Batata Frita G');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Onion Rings', 'Anéis de cebola empanados', 14.90,
       (SELECT id FROM categories WHERE name = 'Acompanhamentos'), 4, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Onion Rings');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Nuggets', 'Porção com 6 unidades de nuggets de frango', 12.90,
       (SELECT id FROM categories WHERE name = 'Acompanhamentos'), 5, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Nuggets');

-- Bebidas
INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Refrigerante Lata', 'Coca-Cola, Guaraná, Fanta ou Sprite - 350ml', 6.90,
       (SELECT id FROM categories WHERE name = 'Bebidas'), 1, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Refrigerante Lata');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Refrigerante 600ml', 'Coca-Cola, Guaraná, Fanta ou Sprite - 600ml', 9.90,
       (SELECT id FROM categories WHERE name = 'Bebidas'), 2, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Refrigerante 600ml');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Suco Natural', 'Laranja, Limão, Abacaxi ou Maracujá - 400ml', 8.90,
       (SELECT id FROM categories WHERE name = 'Bebidas'), 3, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Suco Natural');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Água Mineral', 'Sem gás - 500ml', 4.90,
       (SELECT id FROM categories WHERE name = 'Bebidas'), 4, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Água Mineral');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Água Mineral com Gás', 'Com gás - 500ml', 5.90,
       (SELECT id FROM categories WHERE name = 'Bebidas'), 5, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Água Mineral com Gás');

-- Sobremesas
INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Milkshake', 'Chocolate, Morango ou Baunilha - 400ml', 12.90,
       (SELECT id FROM categories WHERE name = 'Sobremesas'), 1, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Milkshake');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Sorvete', 'Casquinha com uma bola de sorvete', 7.90,
       (SELECT id FROM categories WHERE name = 'Sobremesas'), 2, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Sorvete');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Sundae', 'Sorvete com calda de chocolate ou morango', 9.90,
       (SELECT id FROM categories WHERE name = 'Sobremesas'), 3, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Sundae');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Brownie', 'Brownie de chocolate com sorvete', 13.90,
       (SELECT id FROM categories WHERE name = 'Sobremesas'), 4, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Brownie');

INSERT INTO products (name, description, price, category_id, display_order, active)
SELECT 'Torta de Limão', 'Fatia de torta de limão', 10.90,
       (SELECT id FROM categories WHERE name = 'Sobremesas'), 5, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Torta de Limão');

-- Estoque
INSERT INTO stock (product_id, quantity, updated_at)
SELECT id, 50, NOW()
FROM products
WHERE NOT EXISTS (
  SELECT 1 FROM stock WHERE product_id = products.id
);