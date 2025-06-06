-- Migration: add_fixed_revenue_table
CREATE TABLE fixed_revenue (
    id BIGSERIAL PRIMARY KEY, 
    name TEXT NOT NULL,
    -- 고정 수입 금액
    value BIGINT NOT NULL,
    notification_date DATE NOT NULL,
    date DATE NOT NULL,
    user_id BIGINT NOT NULL,
    account_book_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE INDEX idx_fixed_revenue_user_id
  ON fixed_revenue(user_id);

CREATE INDEX idx_fixed_revenue_account_book_id
  ON fixed_revenue(account_book_id);

