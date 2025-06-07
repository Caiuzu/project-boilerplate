--liquibase formatted sql

--changeset user:01-user-indexes runAlways:true
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(active);

-- Garante CPF único quando document NÃO for NULL
CREATE UNIQUE INDEX IF NOT EXISTS ux_users_document ON users(document)
    WHERE document IS NOT NULL;

-- Garante email único quando email NÃO for NULL
CREATE UNIQUE INDEX IF NOT EXISTS ux_users_email ON users(email)
    WHERE email IS NOT NULL;

-- Garante username único quando username NÃO for NULL
CREATE UNIQUE INDEX IF NOT EXISTS ux_users_username ON users(username)
    WHERE username IS NOT NULL;
