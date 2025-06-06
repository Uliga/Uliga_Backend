-- Migration: add_year_month_week_column_to_revenue_and_expense
ALTER TABLE revenue ADD COLUMN year TEXT NOT NULL;

ALTER TABLE revenue ADD COLUMN month TEXT NOT NULL;

ALTER TABLE revenue ADD COLUMN week TEXT NOT NULL;

ALTER TABLE expense ADD COLUMN year TEXT NOT NULL;

ALTER TABLE expense ADD COLUMN month TEXT NOT NULL;

ALTER TABLE expense ADD COLUMN week TEXT NOT NULL;