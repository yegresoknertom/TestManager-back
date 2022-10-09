-- schema owner
CREATE USER testuser_admin WITH password 'testuser_admin';

CREATE DATABASE testusers WITH OWNER testuser_admin CONNECTION LIMIT 100;

\connect testusers;

-- create schema
CREATE SCHEMA testusers AUTHORIZATION testuser_admin;

-- add-privileges
GRANT USAGE ON SCHEMA testusers TO testuser_admin;

alter user testuser_admin SET search_path = testusers;