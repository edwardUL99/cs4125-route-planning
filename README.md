# Route-Planning-System

Route Planning system for CS4125 module project. 

## Requirements
- Maven 3.6.3 installed
- Java 11 minimum

## Database
To setup the database for this project, ensure MySQL is installed and running on localhost:3306 and run the following commands:
```bash
sudo mysql
mysql> CREATE DATABASE routeplanning;
mysql> CREATE USER route_planning_user@'localhost' IDENTIFIED BY 'route_planning_user_pass';
mysql> GRANT ALL PRIVILEGES ON routeplanning.* TO route_planning_user@'localhost';
mysql> FLUSH PRIVILEGES;
```

Now the application should start up successfully