DROP DATABASE IF EXISTS curiositydb;
DROP USER IF EXISTS curiosity;
CREATE DATABASE curiositydb;
CREATE USER 'curiosity'@'%' IDENTIFIED BY 'Welcome#1';
GRANT ALL PRIVILEGES ON curiositydb.* TO 'curiosity'@'%'; 
SHOW DATABASES;

