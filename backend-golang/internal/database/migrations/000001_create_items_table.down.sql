-- internal/database/migrations/000001_create_items_table.down.sql
DROP TRIGGER IF EXISTS set_items_timestamp ON items;
DROP FUNCTION IF EXISTS trigger_set_timestamp();
DROP TABLE IF EXISTS items; 