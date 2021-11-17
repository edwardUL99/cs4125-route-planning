#! /usr/bin/env bash

# This script installs the route-planning.service file into the systemd service and restarts it

cd "/home/ec2-user/route-planning/source"

cp aws/service/route-planning.service /etc/systemd/system/
systemctl daemon-reload