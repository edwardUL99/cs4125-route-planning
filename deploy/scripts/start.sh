#! /usr/bin/env bash

source /bin/route_planning_env.sh

cd "$ROUTE_PLANNING_CODE"

# This script is responsible for (re)starting the service after an install
deploy/scripts/route-planning.sh restart