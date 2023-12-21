#!/bin/bash

FILE=$1
INTERVAL=10

# Wait for the config file to exist
while [ ! -f "$FILE" ]; do
  echo "Waiting for config file..."
  sleep $INTERVAL
done

exec java -jar gatling-runner/build/libs/gatling-runner-all.jar
