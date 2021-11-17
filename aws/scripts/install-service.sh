#! /usr/bin/env bash

# This script installs the route-planning.service file into the systemd service and restarts it

cp aws/service/route-planning.service /etc/systemd/system/
systemctl daemon-reload