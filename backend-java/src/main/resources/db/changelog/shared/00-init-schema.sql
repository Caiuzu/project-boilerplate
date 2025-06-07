--liquibase formatted sql

--changeset shared:00-init-schema context:local,dev,prod
-- Tipos enumerados (necessários em todos os ambientes)
-- Enum para tipo de pagamento
--changeset payment-types:create-payment-type-enum runOnChange:true
CREATE TYPE payment_type_enum AS ENUM ('CREDIT_CARD', 'DEBIT_CARD', 'PIX', 'ACCOUNT_MONEY');

-- Enum para status de pagamento
--changeset payment-types:create-payment-status-enum runOnChange:true
CREATE TYPE payment_status_enum AS ENUM ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED');

-- Enum para status de pedido
--changeset order-types:create-order-status-enum runOnChange:true
CREATE TYPE order_status_enum AS ENUM ('RECEIVED', 'PREPARING', 'READY', 'COMPLETED', 'CANCELLED');
