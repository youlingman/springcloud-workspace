#!/bin/bash

mvn clean package
rm app.jar
mv target/*.jar app.jar
./deploy.sh stop
./deploy.sh rm
docker image rm cyl/producer
docker build -t cyl/producer .
