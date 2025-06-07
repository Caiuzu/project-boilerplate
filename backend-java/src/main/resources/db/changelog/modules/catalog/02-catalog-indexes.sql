--liquibase formatted sql

--changeset catalog:02-catalog-indexes

-- Índices para tabela de catálogos
CREATE INDEX idx_catalog_name ON catalog(name);

-- Índices para tabela de categorias
CREATE INDEX idx_categories_name ON categories(name);
CREATE INDEX idx_categories_active ON categories(active);
CREATE INDEX idx_categories_display_order ON categories(display_order);

-- Índices para tabela de produtos
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_active ON products(active);
CREATE INDEX idx_products_display_order ON products(display_order);
CREATE INDEX idx_products_price ON products(price);

-- Índices para tabela de estoque
CREATE INDEX IF NOT EXISTS idx_stock_product_id ON stock (product_id);
CREATE INDEX IF NOT EXISTS idx_stock_updated_at ON stock (updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_stock_low_quantity ON stock (quantity) WHERE quantity < 10;