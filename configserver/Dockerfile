#start with base image containing  java runtime
FROM openjdk:17-slim as build

#Information around who maintains the image
MAINTAINER anpgn.com

#Add the applications jar to container
COPY build/libs/configserver-0.0.1-SNAPSHOT.jar  configserver-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java","-jar","/configserver-0.0.1-SNAPSHOT.jar"]