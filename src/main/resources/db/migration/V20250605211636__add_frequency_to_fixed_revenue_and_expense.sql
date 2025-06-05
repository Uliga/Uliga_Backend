-- Migration: add_frequency_to_fixed_revenue_and_expense
CREATE TYPE frequency AS ENUM (
  'DAILY',    -- 매일
  'WEEKLY',   -- 매주
  'MONTHLY',  -- 매월
  'YEARLY'    -- 매년
);

ALTER TABLE fixed_revenue
  ADD COLUMN start_date DATE NOT NULL,
  ADD COLUMN frequency frequency NOT NULL DEFAULT 'MONTHLY';

ALTER TABLE fixed_expense
  ADD COLUMN start_date DATE NOT NULL,
  ADD COLUMN frequency frequency NOT NULL DEFAULT 'MONTHLY';

ALTER TABLE fixed_revenue
  DROP COLUMN notification_date,
  DROP COLUMN date;

ALTER TABLE fixed_expense
  DROP COLUMN notification_date,
  DROP COLUMN date;