-- Migration: init_account_book
CREATE TABLE account_book (
    id BIGSERIAL PRIMARY KEY,
    is_private BOOLEAN,
    name TEXT,
    relation_ship TEXT,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);
