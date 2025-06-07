-- Migration: add_alias_name_column_to_account_book
ALTER TABLE account_book
  DROP COLUMN relation_ship,
  ADD COLUMN alias_name TEXT NOT NULL;
