#!/bin/bash

mvn clean package
rm app.jar
mv target/eureka-service*.jar app.jar
./deploy.sh stop
./deploy.sh rm
docker image rm cyl/eureka-service
docker build -t cyl/eureka-service .
