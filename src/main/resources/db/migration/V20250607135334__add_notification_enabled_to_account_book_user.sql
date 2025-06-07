-- Migration: add_notification_enabled_to_account_book_user
ALTER TABLE account_book_user
  DROP COLUMN get_notification,
  ADD COLUMN notifications_enabled BOOLEAN NOT NULL;
