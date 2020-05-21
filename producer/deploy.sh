#!/bin/bash

COMMAND=$1
SERVICES=("producer1" "producer2")

echo "$COMMAND"
# start all container
if [ "$COMMAND" = "start" ]; then
    for s in ${SERVICES[*]}; do
        docker start `docker ps -a | grep "$s" | awk '{print \$1}'`
    done
fi

# stop all container
if [ "$COMMAND" = "stop" ]; then
    for s in ${SERVICES[*]}; do
        docker stop `docker ps -a | grep "$s" | awk '{print \$1}'`
    done
fi

# rm all container
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
            docker run -d -p $(expr 8100 + $i):8080 --name "${SERVICES[i]}" --network=eureka-service --hostname "${SERVICES[i]}" cyl/producer --spring.profiles.active="${SERVICES[i]}"
        fi
    done
fi
