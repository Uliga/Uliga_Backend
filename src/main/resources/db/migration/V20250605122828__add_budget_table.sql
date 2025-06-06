-- Migration: add_budget_table

CREATE TABLE budget (
    id BIGSERIAL PRIMARY KEY,
    value BIGINT NOT NULL,
    year BIGINT NOT NULL,
    month BIGINT NOT NULL,
    category_id BIGINT,
    account_book_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_budget_category_id
  ON budget(category_id);

CREATE INDEX idx_budget_account_book_id
  ON budget(account_book_id);