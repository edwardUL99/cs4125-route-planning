#! /usr/bin/env bash

# This script uses maven to build the target jar and installs the service

PROPERTIES="/home/ec2-user/route-planning/spring.properties"
CODE="/home/ec2-user/route-planning/source"

cd "$CODE"

SKIP_TESTS="$1"

if [ "$SKIP_TESTS" == "-skip-tests" ]; then
	SKIP_TESTS="-DskipTests"
else
	SKIP_TESTS=""
fi

echo "Restoring original application.properties file"
cp "$PROPERTIES" "$CODE/src/main/resources/application.properties"

echo "Building code..."

LOG="/home/ec2-user/route-planning/build.log"

command="mvn clean package spring-boot:repackage"

if [ ! -z "$SKIP_TESTS" ]; then
	command="$command $SKIP_TESTS"
fi

echo "$command"
$command > $LOG 2>&1
mvn_exit="$?"

echo "Installing service file"
aws/scripts/install-service.sh

echo "Backing up revision to AWS S3"
current_date=$(date +"%m_%d_%Y_%H_%M_%S")
backup_dir="route-planning_$current_date"
cd ../..
cp -r route-planning "$backup_dir"
aws s3 cp --recursive "$backup_dir" s3://route-planning-bucket/revisions/"$backup_dir"
rm -rf "$backup_dir"

if [ "$mvn_exit" -ne "0" ]; then
	echo "Build failed, see $LOG"
	exit 1
else
	echo "Build complete, you should now restart the app by running ./route-planning.sh restart"
fi