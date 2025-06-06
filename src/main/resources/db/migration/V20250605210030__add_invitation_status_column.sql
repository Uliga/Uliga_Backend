-- Migration: add_invitation_status_column
CREATE TYPE invitation_status AS ENUM (
  'PENDING',
  'DECLINED',
  'ACCEPTED'
);

ALTER TABLE account_book_invitation
ADD COLUMN status invitation_status NOT NULL DEFAULT 'PENDING';