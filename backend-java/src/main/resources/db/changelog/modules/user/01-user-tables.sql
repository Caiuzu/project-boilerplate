--liquibase formatted sql

--changeset user:01-user-tables runAlways:true
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    );

COMMENT ON TABLE roles IS 'Tabela de roles/perfis do sistema';
COMMENT ON COLUMN roles.id IS 'ID único da Role';
COMMENT ON COLUMN roles.name IS 'Nome único do role (ex: ADMIN, USER)';
COMMENT ON COLUMN roles.description IS 'Descrição das permissões do role';
COMMENT ON COLUMN roles.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN roles.updated_at IS 'Data da última atualização do registro';

--changeset user:02-user-tables runAlways:true
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    username VARCHAR(50),
    email VARCHAR(255),
    password VARCHAR(255),
    document VARCHAR(255),
    active BOOLEAN DEFAULT TRUE NOT NULL,
    guest BOOLEAN DEFAULT FALSE NOT NULL,
    role_id INTEGER NOT NULL,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

COMMENT ON TABLE users IS 'Tabela de usuários do sistema';
COMMENT ON COLUMN users.id IS 'ID único do usuário';
COMMENT ON COLUMN users.name IS 'Nome do usuário';
COMMENT ON COLUMN users.username IS 'Nome de usuário único para login';
COMMENT ON COLUMN users.email IS 'E-mail do usuário (também único)';
COMMENT ON COLUMN users.password IS 'Hash da senha do usuário';
COMMENT ON COLUMN users.document IS 'Documento do usuário';
COMMENT ON COLUMN users.active IS 'Indica se o usuário está ativo';
COMMENT ON COLUMN users.guest IS 'Indica se o usuário é um convidado (não registrado, guest)';
COMMENT ON COLUMN users.role_id IS 'Id da role do usuário';
COMMENT ON COLUMN users.last_login IS 'Data do último login';
COMMENT ON COLUMN users.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN users.updated_at IS 'Data da última atualização do registro';