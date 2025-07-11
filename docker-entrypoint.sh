#!/bin/sh

set -e

java -XX:+UseParallelGC -XX:InitialRAMPercentage=50 -XX:MaxRAMPercentage=80 -jar app.jar