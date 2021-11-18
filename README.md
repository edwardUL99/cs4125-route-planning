# Route-Planning-System

Route Planning system for CS4125 module project. 

## Requirements
- Maven 3.6.3 installed
- Java 11 minimum

## Setup
To setup the database for this project, ensure MySQL is installed and running on localhost:3306 and run the following commands:
```bash
sudo mysql
mysql> CREATE DATABASE <database-name>;
mysql> CREATE USER <username>@'localhost' IDENTIFIED BY '<password>';
mysql> GRANT ALL PRIVILEGES ON <database-name>.* TO <username>@'localhost';
mysql> FLUSH PRIVILEGES;
```
Change `<database-name>`, `<username>` and `<password>` to values you wish

In src/main/resources/application.properties, change the following properties:
```
spring.datasource.url=jdbc:mysql://localhost:3306/<database-name>
spring.datasource.username=<username>
spring.datasource.password=<password>
```
Again, change the values in triangle brackets to the values you used setting up the database.

Now, run the following command:
```bash
mvn clean package spring-boot:repackage
```

When the build completes, run the command:
```bash
java --version # validate that you have at least Java 11
java -jar target/route-planning-1.0-SNAPSHOT.jar
```
Wait for the application to start, and then you should be able to access the service on http://localhost:8080