#!/bin/bash

COMMAND=$1
SERVICES=("eureka-service1" "eureka-service2" "eureka-service3")

echo "$COMMAND"
# start all eureka-service container
if [ "$COMMAND" = "start" ]; then
    for s in ${SERVICES[*]}; do
        docker start `docker ps -a | grep "$s" | awk '{print \$1}'`
    done
fi

# stop all eureka-service container
if [ "$COMMAND" = "stop" ]; then
    for s in ${SERVICES[*]}; do
        docker stop `docker ps -a | grep "$s" | awk '{print \$1}'`
    done
fi

# rm all eureka-service container
if [ "$COMMAND" = "rm" ]; then
    for s in ${SERVICES[*]}; do
        docker rm `docker ps -a | grep "$s" | awk '{print \$1}'`
    done
fi

# init network and container
if [ "$COMMAND" = "init" ]; then
    NETWORK=`docker network ls | grep eureka-service | awk '{print \$1}'`
    if [ -z "$NETWORK" ]; then
        docker network create --driver bridge eureka-service
    fi
    for i in $(seq 0 ${#SERVICES[@]}); do
        if [ ! "" = "${SERVICES[i]}" ]; then
            docker run -d -p $(expr 9100 + $i):8761 --name "${SERVICES[i]}" --network=eureka-service --hostname "${SERVICES[i]}" cyl/eureka-service --spring.profiles.active="${SERVICES[i]}"
        fi
    done
fi
