--liquibase formatted sql

--changeset shared:01-modulith-tables context:local,dev,prod
-- Tabela utilizada pelo Spring Modulith para persistência de eventos
CREATE TABLE IF NOT EXISTS event_publication (
    id UUID NOT NULL PRIMARY KEY,
    listener_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    serialized_event TEXT NOT NULL,
    publication_date TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    completion_date TIMESTAMP(6) WITH TIME ZONE
);

-- Índices recomendados para performance e controle
CREATE INDEX IF NOT EXISTS idx_event_pub_listener ON event_publication(listener_id);
CREATE INDEX IF NOT EXISTS idx_event_pub_incomplete ON event_publication(completion_date);
CREATE INDEX IF NOT EXISTS idx_event_pub_type ON event_publication(event_type);
CREATE INDEX IF NOT EXISTS idx_event_pub_publication_date ON event_publication(publication_date); 
