#! /usr/bin/env bash

ARGUMENT="$1"

if [ -z "$ARGUMENT" ]; then
	ARGUMENT="start"
fi

command="sudo systemctl $ARGUMENT route-planning"

echo "$command"
$command
