CREATE TABLE IF NOT EXISTS CompanyTicker (
--DROP TABLE IF EXISTS CompanyTicker;
--CREATE TABLE CompanyTicker (
    id SERIAL PRIMARY KEY,
    cik_str VARCHAR(50) NOT NULL,
    ticker VARCHAR(10) NOT NULL,
    title VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ
);
