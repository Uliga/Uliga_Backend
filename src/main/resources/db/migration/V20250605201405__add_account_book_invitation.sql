-- Migration: add_account_book_invitation
CREATE TABLE account_book_invitation (
    id BIGSERIAL PRIMARY KEY,
    account_book_id BIGINT NOT NULL,
    inviter_user_id BIGINT NOT NULL,
    invitee_user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_account_book_invitation_account_book_id
  ON account_book_invitation(account_book_id);

CREATE INDEX idx_account_book_invitation_inviter_user_id
  ON account_book_invitation(inviter_user_id);

CREATE INDEX idx_account_book_invitation_invitee_user_id
    ON account_book_invitation(invitee_user_id);
