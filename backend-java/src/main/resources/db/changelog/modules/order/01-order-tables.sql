--liquibase formatted sql

--changeset order:01-order-tables runAlways:true
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    order_number VARCHAR(255) UNIQUE NOT NULL,
    status order_status_enum DEFAULT 'RECEIVED' NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id)
);

COMMENT ON TABLE orders IS 'Tabela que armazena os pedidos do sistema';
COMMENT ON COLUMN orders.id IS 'Identificador único do pedido';
COMMENT ON COLUMN orders.user_id IS 'Referência ao usuário que fez o pedido';
COMMENT ON COLUMN orders.order_number IS 'Número único identificador do pedido para negócio';
COMMENT ON COLUMN orders.status IS 'Status atual do pedido (usando tipo enumerado)';
COMMENT ON COLUMN orders.amount IS 'Valor total do pedido em reais';
COMMENT ON COLUMN orders.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN orders.updated_at IS 'Data da última atualização do registro';

--changeset order:02-order-tables runAlways:true
CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    observations TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);

COMMENT ON TABLE order_items IS 'Tabela que armazena os itens de cada pedido';
COMMENT ON COLUMN order_items.id IS 'Identificador único do item do pedido';
COMMENT ON COLUMN order_items.order_id IS 'Referência ao pedido associado';
COMMENT ON COLUMN order_items.product_id IS 'Referência ao produto vendido';
COMMENT ON COLUMN order_items.name IS 'Nome do produto comercializado no momento da venda';
COMMENT ON COLUMN order_items.quantity IS 'Quantidade do produto no pedido';
COMMENT ON COLUMN order_items.unit_price IS 'Preço unitário do produto no momento da venda';
COMMENT ON COLUMN order_items.observations IS 'Observações específicas sobre o item';
COMMENT ON COLUMN order_items.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN order_items.updated_at IS 'Data da última atualização do registro';
