-- Migration: add_account_book_data_index
CREATE INDEX idx_account_book_data_member_id
    ON account_book_data(member_id);

