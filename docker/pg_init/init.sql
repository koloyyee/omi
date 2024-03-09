SELECT 'CREATE DATABASE breakfast' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'breakfast');
SELECT 'CREATE DATABASE besure' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'besure');
