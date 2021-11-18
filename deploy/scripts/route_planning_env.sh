#! /usr/bin/env bash

# This script sets environment variables for the project

# The root of the application and all its artifacts
export ROUTE_PLANNING_ROOT="/home/ec2-user/route-planning"
# The version of application properties being used for this deployment
export ROUTE_PLANNING_PROPERTIES="$ROUTE_PLANNING_ROOT/spring.properties"
# The directory containing the application code and resources
export ROUTE_PLANNING_CODE="$ROUTE_PLANNING_ROOT/source"
# The version of the JAR file being used
export ROUTE_PLANNING_VERSION="1.0-SNAPSHOT"