#! /usr/bin/env bash

source /bin/route_planning_env.sh

cd "$ROUTE_PLANNING_CODE"

java -jar "target/route-planning-$ROUTE_PLANNING_VERSION.jar"