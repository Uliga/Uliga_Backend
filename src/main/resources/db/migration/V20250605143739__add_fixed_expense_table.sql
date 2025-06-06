-- Migration: add_fixed_expense_table
CREATE TABLE fixed_expense(
    id BIGSERIAL PRIMARY KEY, 
    name TEXT NOT NULL,
    -- 고정 지출 금액
    value BIGINT NOT NULL,
    notification_date DATE NOT NULL,
    date DATE NOT NULL,
    user_id BIGINT NOT NULL,
    account_book_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE INDEX idx_fixed_expense_user_id
  ON fixed_expense(user_id);

CREATE INDEX idx_fixed_expense_account_book_id
  ON fixed_expense(account_book_id);

