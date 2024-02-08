SELECT 'CREATE DATABASE Breakfast' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'Breakfast');
SELECT 'CREATE DATABASE Mesure' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'Mesure');

--CREATE DATABASE Breakfast;
--CREATE DATABASE Mesure;
