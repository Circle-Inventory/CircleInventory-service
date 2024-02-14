DROP DATABASE TOHO;
create database TOHO;
use TOHO;

CREATE TABLE Category (
    category_id VARCHAR(50) NOT NULL,
    category_name VARCHAR(255),
    description VARCHAR(255),
    total_items BIGINT DEFAULT 0,
    date_added DATE,
    image_url VARCHAR(225),
    tag VARCHAR(255),
    status BOOLEAN,
    PRIMARY KEY (category_id)
);

CREATE TABLE Items (
	item_id VARCHAR(50),
    item_name VARCHAR(255),
    brand_name VARCHAR(255),
    category VARCHAR(255),
    quantity INT,
    year INT,
    gross_price DOUBLE,
    model_number VARCHAR(255),
    color VARCHAR(255),
    tags VARCHAR(255),
    net_price DOUBLE,
    image_url VARCHAR(255),
    PRIMARY KEY (item_id)
);

CREATE TABLE Users (
    user_id VARCHAR(50) PRIMARY KEY,
    last_name VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255),
    first_name VARCHAR(255),
    image_url VARCHAR(255),
    role VARCHAR(255)
);

CREATE TABLE Tokens (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	token VARCHAR(255) NOT NULL UNIQUE,
	user_id VARCHAR(255) NOT NULL,
	created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_user_token FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE ApprovalRequests (
    request_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50),
    category_name VARCHAR(50),
    item_name VARCHAR(255),
    brand_name VARCHAR(255),
    quantity INT,
    gross_price DOUBLE,
    net_price DOUBLE,
    status BOOLEAN DEFAULT FALSE,
    date_submitted DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Report (
    report_id INT PRIMARY KEY,
    type VARCHAR(255),
    date DATE,
    data VARCHAR(255)
);
