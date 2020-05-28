#!/bin/bash

COMMAND=$1
SERVICES=("config-service")

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
            docker run -d -p $(expr 8888 + $i):8888 --name "${SERVICES[i]}" --network=eureka-service --hostname "${SERVICES[i]}" --restart always cyl/config-service
        fi
    done
fi
