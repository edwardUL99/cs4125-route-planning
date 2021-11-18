#! /usr/bin/env bash

# This script uses maven to build the target jar and installs the service

source /bin/route_planning_env.sh

cd "$ROUTE_PLANNING_CODE"

SKIP_TESTS="$1"

if [ "$SKIP_TESTS" == "-skip-tests" ]; then
	SKIP_TESTS="-DskipTests"
else
	SKIP_TESTS=""
fi

current_date=$(date +"%m_%d_%Y_%H_%M_%S")

echo "Restoring original application.properties file"
cp "$ROUTE_PLANNING_PROPERTIES" "$ROUTE_PLANNING_CODE/src/main/resources/application.properties"

echo "Building code..."

LOG="$ROUTE_PLANNING_ROOT/build.log.$current_date"

command="mvn clean package spring-boot:repackage"

if [ ! -z "$SKIP_TESTS" ]; then
	command="$command $SKIP_TESTS"
fi

echo "$command"
$command > "$LOG" 2>&1
mvn_exit="$?"

if [ "$mvn_exit" -ne "0" ]; then
	echo "Build failed, see $LOG"
	exit 1
fi

echo "Installing service file"
deploy/scripts/install-service.sh

echo "Backing up revision"
backup_dir="route-planning_$current_date"
cd ../..

if [ ! -d "./revisoons" ]; then
	mkdir revisions
fi

cp -r route-planning "revisions/$backup_dir"

echo "Build complete, you should now restart the app by running $ROUTE_PLANNING_CODE/deploy/scripts/route-planning.sh restart"

