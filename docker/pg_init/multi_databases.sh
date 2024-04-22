#!/bin/bash

set -e
set -u
# reference to https://dev.to/bgord/multiple-postgres-databases-in-a-single-docker-container-417l
# github: https://github.com/mrts/docker-postgresql-multiple-databases
function create_user_and_database() {
	local database=$1
	echo "  Creating user and database '$database'"
	psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE USER $database;
	    CREATE DATABASE $database;
	    GRANT ALL PRIVILEGES ON DATABASE $database TO $database;
EOSQL
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
	echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
	for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
	  echo $db;
		create_user_and_database $db
	done
	echo "Multiple databases created"
fi