-- Migration: add_join_table
CREATE TABLE account_book_user (
    id BIGSERIAL PRIMARY KEY, 
    account_book_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    profile_url TEXT,
    get_notification BOOLEAN NOT NULL DEFAULT FALSE,
    account_book_authority account_book_authority NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);


CREATE INDEX idx_account_book_user_account_book_id
  ON account_book_user(account_book_id);

CREATE INDEX idx_account_book_user_user_id
  ON account_book_user(user_id);

CREATE TABLE fixed_expense_user (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  account_book_id BIGINT NOT NULL,
  fixed_expense_id BIGINT NOT NULL,
  value BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_fixed_expense_user_user_id
  ON fixed_expense_user(user_id);

CREATE INDEX idx_fixed_expense_user_account_book_id
  ON fixed_expense_user(account_book_id);

CREATE INDEX idx_fixed_expense_user_fixed_expense_id
  ON fixed_expense_user(fixed_expense_id);