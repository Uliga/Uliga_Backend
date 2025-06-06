-- Migration: add_category_table

CREATE TABLE revenue_category (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    account_book_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_revenue_category_account_book_id
    ON revenue_category(account_book_id);

CREATE TABLE expense_category (
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  account_book_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_expense_category_account_book_id
  ON expense_category(account_book_id);