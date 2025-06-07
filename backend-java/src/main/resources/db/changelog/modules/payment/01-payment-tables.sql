--liquibase formatted sql

--changeset payment:01-payment-tables runAlways:true
CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    type payment_type_enum,
    expires_in TIMESTAMP NOT NULL,
    status payment_status_enum DEFAULT 'PENDING' NOT NULL,
    paid_at TIMESTAMP,
    tid VARCHAR(255) UNIQUE,
    amount DECIMAL(10, 2) NOT NULL,
    qr_code VARCHAR(255) NOT NULL,
    observations TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_payment_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT chk_paid_at_if_approved CHECK (status != 'APPROVED' OR paid_at IS NOT NULL),
    CONSTRAINT chk_type CHECK (status = 'PENDING' OR status = 'CANCELLED' OR type IS NOT NULL),
    CONSTRAINT chk_tid CHECK (status = 'PENDING' OR status = 'CANCELLED' OR tid IS NOT NULL)
);

COMMENT ON TABLE payments IS 'Tabela que armazena os pagamentos do sistema';
COMMENT ON COLUMN payments.id IS 'Identificador único do pagamento';
COMMENT ON COLUMN payments.user_id IS 'Referência ao usuário associado ao pagamento';
COMMENT ON COLUMN payments.order_id IS 'Referência ao pedido associado ao pagamento';
COMMENT ON COLUMN payments.type IS 'Tipo de pagamento';
COMMENT ON COLUMN payments.status IS 'Status do pagamento';
COMMENT ON COLUMN payments.paid_at IS 'Data e hora em que o pagamento foi confirmado';
COMMENT ON COLUMN payments.expires_in IS 'Data e hora de expiração do pagamento';
COMMENT ON COLUMN payments.tid IS 'Identificador da transação no gateway de pagamento';
COMMENT ON COLUMN payments.amount IS 'Valor total do pagamento em reais';
COMMENT ON COLUMN payments.qr_code IS 'Conteúdo do QR Code para pagamentos (quando aplicável)';
COMMENT ON COLUMN payments.observations IS 'Observações adicionais sobre o pagamento';
COMMENT ON COLUMN payments.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN payments.updated_at IS 'Data da última atualização do registro';
COMMENT ON CONSTRAINT chk_paid_at_if_approved ON payments IS 'Se o status do pagamento for APPROVED, o campo paid_at deve ser obrigatoriamente preenchido.';
COMMENT ON CONSTRAINT chk_type ON payments IS 'Se o status do pagamento não for PENDING/CANCELLED, o campo type deve ser obrigatoriamente preenchido.';
COMMENT ON CONSTRAINT chk_tid ON payments IS 'Se o status do pagamento não for PENDING/CANCELLED, o campo tid deve ser obrigatoriamente preenchido.';