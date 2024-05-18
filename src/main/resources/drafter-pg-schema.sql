CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    role VARCHAR(100) NOT NULL
);
CREATE TABLE  IF NOT EXISTS authorities (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

--create unique index if not exists  ix_auth_username on authorities (
--user_id,
--authority);

create TABLE IF NOT EXISTS Applicant (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL CONSTRAINT fk_applicant_user REFERENCES users(id)
);
create TABLE IF NOT EXISTS Resume (
    id SERIAL PRIMARY KEY,
    user_id INTEGER CONSTRAINT fk_resume_appliant_id REFERENCES applicant(id)
);

create table if not exists industry (
    id serial primary key,
    industry_name varchar(255)
);

create table if not exists company (
    id serial primary key,
    company_name varchar(255),
    company_description text,
    industry_id int constraint fk_company_industry_id references industry(id)
);

create table if not exists status (
    id serial primary key,
    status_name varchar(20) default 'waiting' not null unique
);

create TABLE IF NOT EXISTS Application (
    ID SERIAL PRIMARY KEY,
    job_title varchar(255) not null,
    job_id varchar(50),
    remark text,
    company_id integer constraint fk_application_company_id references company(id),
    status_id integer constraint fk_application_status_id references status(id),
    created_at timestamptz not null,
    updated_at timestamptz
);

create table if not exists applicant_application (
    applicant_id int not null,
    application_id int not null,
    primary key (applicant_id, application_id)
);


-------------------
--insert into users
--(username, password, email, enabled, role)
--values
--('david',
--'{bcrypt}$2a$10$kz2iFhO0fzLNrU5mRl1eNuPFHzCENDCkC.0NPk55vKiKngdPKQjku',
--'admin@test.com',
--true,
--'ADMIN')
