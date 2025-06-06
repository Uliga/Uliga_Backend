-- Migration: refactor_to_revenue_category_in_budget_table
DROP INDEX idx_budget_category_id;

ALTER TABLE budget DROP COLUMN category_id;

ALTER TABLE budget ADD COLUMN expense_category_id BIGINT;

CREATE INDEX idx_budget_expense_category_id
  ON budget(expense_category_id);