FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY app.jar app.jar
ENTRYPOINT java -jar /app.jar $0 $@ 1>log 2>&1