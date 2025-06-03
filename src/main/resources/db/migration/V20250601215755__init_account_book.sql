-- Migration: init_account_book
CREATE TABLE AccountBook (
    id BIGSERIAL PRIMARY KEY,
    isPrivate BOOLEAN,
    name TEXT,
    relationShip TEXT,
    createdAt TIMESTAMP DEFAULT now(),
    updatedAt TIMESTAMP DEFAULT now()
);
