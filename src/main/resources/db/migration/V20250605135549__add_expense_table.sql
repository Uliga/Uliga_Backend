-- Migration: add_expense_table
CREATE TABLE expense (
    id BIGSERIAL PRIMARY KEY, 
    -- 지출 금액
    value BIGINT,
    -- 사용처
    expense_payee TEXT NOT NULL,
    memo TEXT,
    date DATE NOT NULL,
    user_id BIGINT NOT NULL,
    expense_category_id BIGINT,
    account_book_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE INDEX idx_expense_user_id
  ON expense(user_id);

CREATE INDEX idx_expense_category_id
  ON expense(expense_category_id);

CREATE INDEX idx_expense_account_book_id
  ON expense(account_book_id);
