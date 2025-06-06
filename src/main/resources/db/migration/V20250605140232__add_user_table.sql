-- Migration: add_member_table
CREATE TYPE authority AS ENUM (
  'ROLE_USER',
  'ROLE_ADMIN'
);

CREATE TYPE user_login_type AS ENUM (
  'EMAIL',
  'KAKAO',
  'NAVER'
);

CREATE TABLE "user" (
    id BIGSERIAL PRIMARY KEY, 
    email TEXT NOT NULL,
    password TEXT,
    app_password TEXT,
    authority authority NOT NULL DEFAULT 'ROLE_USER',
    user_login_type user_login_type NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    user_name TEXT NOT NULL,
    nick_name TEXT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT NOW()
);

