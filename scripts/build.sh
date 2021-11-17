#! /usr/bin/env bash


PROPERTIES="/home/ec2-user/route-planning/spring.properties"
CODE="/home/ec2-user/route-planning/source"

SKIP_TESTS="$1"

if [ "$SKIP_TESTS" == "-skip-tests" ]; then
	SKIP_TESTS="-DskipTests"
else
	SKIP_TESTS=""
fi

if [ "$PWD" != "$CODE" ]; then
	echo "Changing from $PWD to $CODE"
	cd "$CODE"
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

if [ "$?" -ne "0" ]; then
	echo "Build failed, see $LOG"
	exit 1
else
	echo "Build complete, you should now restart the app by running ./route-planning.sh restart"
fi