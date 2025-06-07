--liquibase formatted sql

--changeset payment:03-payment-seed context:local,dev runAlways:true onError:MARK_RAN
-- Pagamentos para ambiente de desenvolvimento

-- Pagamento 1 (Cartão de Crédito) - João Silva
INSERT INTO payments (
    user_id, order_id, type, expires_in, status, paid_at, tid, amount, qr_code, observations, created_at, updated_at
)
SELECT
    u.id,
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000001'),
    'CREDIT_CARD',
    NOW() + interval '30 days',
    'APPROVED',
    NOW(),
    '112673020299',
    32.80,
    '00020101021243650016COM.MERCADOLIBRE02013063682409123-aaaa-bbbb-cccc-1234567890ab5204000053039865802BR5908Joao Test6009CURITIBA62070503***63AS04A13B',
    'Pagamento aprovado via cartão',
    NOW(),
    NOW()
FROM users u
WHERE u.email = 'joao@email.com'
  AND NOT EXISTS (
      SELECT 1 FROM payments p WHERE p.tid = '112673020299'
  );

-- Pagamento 2 (Débito) - Maria Oliveira
INSERT INTO payments (
    user_id, order_id, type, expires_in, status, paid_at, tid, amount, qr_code, observations, created_at, updated_at
)
SELECT
    u.id,
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000002'),
    'DEBIT_CARD',
    NOW(),
    'APPROVED',
    NOW(),
    '872379520298',
    79.70,
    '000201010asdasd43650016COM.MERCADOLIBRE02013063682409123-aaaa-bbbb-cccc-1234567890ab5204000053039865802BR5908Joao Test6009CURITIBA62070503***63AS04A13B',
    'Pagamento via débito automático',
    NOW(),
    NOW()
FROM users u
WHERE u.email = 'maria@email.com'
  AND NOT EXISTS (
      SELECT 1 FROM payments p WHERE p.tid = '872379520298'
  );

-- Pagamento 3 (PIX) - Maria Oliveira
INSERT INTO payments (
    user_id, order_id, type, expires_in, status, paid_at, tid, amount, qr_code, observations, created_at, updated_at
)
SELECT
    u.id,
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000003'),
    NULL,
    NOW() + interval '1 hour',
    'PENDING',
    NULL,
    '622433528299',
    19.90,
    '00020101021243650016COM.MERCADOLIBRE02013063682409123-aaaa-bbbb-cccc-1234567890ab5204000053039865802BR5908Joao Test6009CURITIBA62070503***6304A13B',
    'QR Code gerado para pagamento',
    NOW(),
    NOW()
FROM users u
WHERE u.email = 'maria@email.com'
  AND NOT EXISTS (
      SELECT 1 FROM payments p WHERE p.tid = '622433528299'
  );

-- Pagamento 4 (PIX) - Maria Oliveira
INSERT INTO payments (
    user_id, order_id, type, expires_in, status, paid_at, tid, amount, qr_code, observations, created_at, updated_at
)
SELECT
    u.id,
    (SELECT id FROM orders WHERE order_number = 'ORD-2025-00000004'),
    NULL,
    NOW() + interval '1 hour',
    'PENDING',
    NULL,
    '2126735727299',
    4.90,
    '00020101021243650016COM.MERCADOLIBRE02013063682409cafe-9876-abcd-1234-1234567890cd5204000053039865802BR5912Maria Silva6009RIO BRANCO62070503***6304C7D2',
    'QR Code gerado para pagamento',
    NOW(),
    NOW()
FROM users u
WHERE u.email = 'maria@email.com'
  AND NOT EXISTS (
      SELECT 1 FROM payments p WHERE p.tid = '2126735727299'
  );