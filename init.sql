-- create initial database
-- Postgres
--create table users(
--       id serial primary key,
--       email varchar(255) not null,
--       password varchar(512) not null
--);
-- Dev db
create table users(
       id int identity primary key,
       email varchar(255) not null,
       password varchar(512) not null
);
