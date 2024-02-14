# service (Inventory back-end)

## Features

- Login and signup function
- Admin can create, delete and update categorys
- Admin can create, delete and update items

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java JDK 21
- Maven
- MySQL Server

## Setting Up MySQL Database

1. Start your MySQL server.
2. Create a new database/schema for the project using given SQL script

### Configuration

Before running the project, configure the application.properties or application.yml file in the src/main/resources directory with your MySQL database connection details:
spring.datasource.url=jdbc:mysql://localhost:3306/toho
spring.datasource.username=username
spring.datasource.password=password

Replace username, and password with your MySQL username, and password, respectively

### Install maven dependencies

Got maven console an run following command

- `mvn clean`
- `mvn install`
