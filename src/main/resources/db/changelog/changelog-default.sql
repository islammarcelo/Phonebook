--liquibase formatted sql
--changeset mk:1
INSERT INTO phonebook_entry (phone, name) VALUES ('12345678', 'Alice');
INSERT INTO phonebook_entry (phone, name) VALUES ('87654321', 'Bob');
INSERT INTO phonebook_entry (phone, name) VALUES ('11223344', 'Charlie');