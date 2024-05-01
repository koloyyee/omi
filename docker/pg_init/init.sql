SELECT 'CREATE DATABASE drafter ' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'drafter');
SELECT 'CREATE DATABASE breakfast' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'breakfast');
SELECT 'CREATE DATABASE mesure' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'mesure');
