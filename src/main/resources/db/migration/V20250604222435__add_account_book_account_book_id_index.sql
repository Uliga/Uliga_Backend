-- Migration: add_account_book_account_book_id_index
CREATE INDEX idx_account_book_data_account_book_id
    ON account_book_data(account_book_id);
