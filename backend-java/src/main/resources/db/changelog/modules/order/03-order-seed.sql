--liquibase formatted sql

--changeset order:03-order-seed runAlways:true context:local,dev onError:MARK_RAN
-- Os dados de pedidos são apenas para ambiente de desenvolvimento

-- Pedido 1
INSERT INTO orders (user_id, order_number, status, amount, created_at, updated_at)
SELECT
    (SELECT id FROM users WHERE email = 'maria@email.com' LIMIT 1),
    'ORD-2025-00000001',
    'COMPLETED',
    32.80,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM orders WHERE order_number = 'ORD-2025-00000001'
);

-- Itens do pedido 1
-- X-Burger
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000001'),
    (SELECT id FROM products WHERE name = 'X-Burger'),
    'X-Burger',
    1,
    22.90,
    'Sem cebola',
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items
    WHERE order_id = (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000001')
    AND product_id = (SELECT id FROM products WHERE name = 'X-Burger')
);

-- Refrigerante Lata
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000001'),
    (SELECT id FROM products WHERE name = 'Refrigerante Lata'),
    'Refrigerante Lata',
    1,
    6.90,
    null,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items
    WHERE order_id = (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000001')
    AND product_id = (SELECT id FROM products WHERE name = 'Refrigerante Lata')
);

-- Pedido 2
INSERT INTO orders (user_id, order_number, status, amount, created_at, updated_at)
SELECT
    (SELECT id FROM users WHERE email = 'joao@email.com'),
    'ORD-2025-00000002',
    'READY',
    79.70,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM orders WHERE order_number = 'ORD-2025-00000002'
);

-- X-Salada
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000002'),
    (SELECT id FROM products WHERE name = 'X-Salada'),
    'X-Salada',
    2,
    20.90,
    null,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items
    WHERE order_id = (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000002')
    AND product_id = (SELECT id FROM products WHERE name = 'X-Salada')
);

-- X-Bacon
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000002'),
    (SELECT id FROM products WHERE name = 'X-Bacon'),
    'X-Bacon',
    1,
    24.90,
    null,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items
    WHERE order_id = (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000002')
    AND product_id = (SELECT id FROM products WHERE name = 'X-Bacon')
);

-- Refrigerante Lata
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000002'),
    (SELECT id FROM products WHERE name = 'Refrigerante Lata'),
    'Refrigerante Lata',
    2,
    6.90,
    null,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items
    WHERE order_id = (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000002')
    AND product_id = (SELECT id FROM products WHERE name = 'Refrigerante Lata')
);

-- Pedido 3
INSERT INTO orders (user_id, order_number, status, amount, created_at, updated_at)
SELECT
    (SELECT id FROM users WHERE email = 'joao@email.com'),
    'ORD-2025-00000003',
    'RECEIVED',
    19.90,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM orders WHERE order_number = 'ORD-2025-00000003'
);

-- Água Mineral
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000003'),
    (SELECT id FROM products WHERE name = 'Água Mineral'),
    'Água Mineral',
    1,
    4.90,
    null,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items
    WHERE order_id = (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000003')
    AND product_id = (SELECT id FROM products WHERE name = 'Água Mineral')
);

-- Batata Frita G
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000003'),
    (SELECT id FROM products WHERE name = 'Batata Frita G'),
    'Batata Frita G',
    1,
    15.00,
    null,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items
    WHERE order_id = (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000003')
    AND product_id = (SELECT id FROM products WHERE name = 'Batata Frita G')
);

-- Pedido 4
INSERT INTO orders (user_id, order_number, status, amount, created_at, updated_at)
SELECT
    (SELECT id FROM users WHERE email = 'joao@email.com'),
    'ORD-2025-00000004',
    'RECEIVED',
    4.90,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM orders WHERE order_number = 'ORD-2025-00000004'
);

-- Água Mineral (único item)
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000004'),
    (SELECT id FROM products WHERE name = 'Água Mineral'),
    'Água Mineral',
    1,
    4.90,
    null,
    now(),
    now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items
    WHERE order_id = (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000004')
    AND product_id = (SELECT id FROM products WHERE name = 'Água Mineral')
);