# Route-Planning-System

Route Planning system for CS4125 module project. 

## Requirements
- Maven 3.6.3 installed
- Java 11 minimum

## AWS Deployment
There is an existing AWS (Amazon Web Services) deployment of the application that is deployed on every push to the main branch that can be found
here: http://www.cs4125routeplanning.ie

It has its own database hosted on AWS also, so it does not need database setup

To deploy the app on your own machine, follow the next few sections

## Database Setup
To setup the database for this project, ensure MySQL is installed and running on localhost:3306 and run the following commands:
```bash
sudo mysql
mysql> CREATE DATABASE <database-name>;
mysql> CREATE USER <username>@'localhost' IDENTIFIED BY '<password>';
mysql> GRANT ALL PRIVILEGES ON <database-name>.* TO <username>@'localhost';
mysql> FLUSH PRIVILEGES;
```
Change `<database-name>`, `<username>` and `<password>` to values you wish

In src/main/resources/application.properties, uncomment the following lines (by removing the # symbols):
```
#spring.datasource.url=jdbc:mysql://localhost:3306/<database-name>
#spring.datasource.username=<username>
#spring.datasource.password=<password>
#spring.jpa.properties.hibernate.dialect=or
```
, then comment out this line (by placing a # in front of it):
```
spring.jpa.hibernate.ddl-auto=create-drop
```
, then change the database name, username and password in the uncommented lines

Again, change the values in triangle brackets to the values you used setting up the database.

## Quick Run
This section shows how you can quickly build and run the app without installing/deploying it. It is easiest to install it
as it will be deployed as a service and restart automatically, however follow these steps to quickly run it. To read about
local deployment, read the next section.

Run the following command:
```bash
mvn clean package spring-boot:repackage
```

When the build completes, run the command:
```bash
java --version # validate that you have at least Java 11
java -jar target/route-planning-1.0-SNAPSHOT.jar
```
Wait for the application to start, and then you should be able to access the service on http://localhost:8080

## Local Deployment
The deployment directory `deploy` is configured for use with AWS. However, if you want to create a local deployment of
the service on your own machine, you can perform the following:

1. Create the application root directory, for example, here, it is /home/user/route-planning: 
`mkdir /home/user/route-planning`                                                                                         
1. Copy the application.properties file from the setup above to a file called spring.properties in the application root directory:
`cp src/main/resources/application.properties /home/user/route-planning/spring.properties`
1. Copy the github repo to a directory called `source` (first `cd ..`): `cp -r <name-of-repo> /home/user/route-planning/source
1. Change to the source directorygedit
1. In `deploy/scripts/route_planning_env.sh`, change ROUTE_PLANNING_ROOT to the root directory:
`export ROUTE_PLANNING_ROOT="/home/user/route-planning"`
1. Copy `deploy/scripts/route_planning_env.sh` and `deploy/scripts/route_planning_service.sh` to /bin, or you can create a symlink:
```bash
sudo ln -s "$PWD/deploy/scripts/route_planning_env.sh" /bin
sudo ln -s "$PWD/deploy/scripts/route_planning_service.sh" /bin
```

Now, the setup is complete and environment variables are configured. You can now build the code and start the service.
Follow these steps:
1. Using sudo, run the following code:
`sudo deploy/scripts/build.sh`
1. When the build completes, you can then start the service by running `sudo deploy/scripts/start.sh`

 