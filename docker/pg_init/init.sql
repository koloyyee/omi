SELECT 'CREATE DATABASE applied' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'applied');
SELECT 'CREATE DATABASE breakfast' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'breakfast');
SELECT 'CREATE DATABASE mesure' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'mesure');
