-- Migration: add_account_book_data
CREATE TYPE account_book_data_type AS ENUM (
    'RECORD',
    'INCOME'
);

CREATE TYPE account_book_authority AS ENUM (
  'USER',
  'ADMIN'
);

-- account_book_data 테이블 생성 (ENUM 타입 사용)
CREATE TABLE account_book_data (
    account_book_data_id BIGSERIAL PRIMARY KEY,
    value                BIGINT,
    payment              TEXT,
    account              TEXT,
    memo                 TEXT,
    date                 DATE,
    type                 account_book_data_type,  -- ENUM 타입 적용
    member_id            BIGINT,
    category_id          BIGINT,
    account_book_id      BIGINT,
    created_at           TIMESTAMP(6) NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP(6) NOT NULL DEFAULT NOW()
);

