#!/bin/bash

mvn clean package
rm app.jar
mv target/eureka-service*.jar app.jar
docker image rm cyl/eureka-service
docker build -t cyl/eureka-service .
