#!/bin/bash

docker run -d -p 9100:8761 --name eureka-service1 cyl/eureka-service
docker run -d -p 9101:8761 --name eureka-service2 cyl/eureka-service
docker run -d -p 9102:8761 --name eureka-service3 cyl/eureka-service
