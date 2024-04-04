CREATE TABLE restaurant (
    id int AUTO_INCREMENT PRIMARY KEY,
    sessionId int,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE session (
    id int AUTO_INCREMENT PRIMARY KEY,
    ended BOOLEAN DEFAULT false,
    username VARCHAR(255) NOT NULL
);

CREATE TABLE user (
    id int AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);