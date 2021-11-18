#! /usr/bin/env bash

# This script is used by the service to run the target JAR

cd /home/ec2-user/route-planning/source
java -jar target/route-planning-1.0-SNAPSHOT.jar
