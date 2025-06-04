-- Migration: add_account_book_category_id_index
CREATE INDEX idx_account_book_data_category_id
    ON account_book_data(category_id);
