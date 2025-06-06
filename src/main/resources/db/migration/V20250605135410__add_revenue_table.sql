-- Migration: add_revenue_table
CREATE TABLE revenue (
    id BIGSERIAL PRIMARY KEY, 
    -- 수입 금액
    value BIGINT NOT NULL,
    -- 수입원
    revenue_source TEXT NOT NULL,
    memo TEXT,
    date DATE NOT NULL,
    user_id BIGINT NOT NULL,
    revenue_category_id BIGINT,
    account_book_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_revenue_user_id
  ON revenue(user_id);

CREATE INDEX idx_revenue_category_id
  ON revenue(revenue_category_id);

CREATE INDEX idx_revenue_account_book_id
  ON revenue(account_book_id);
