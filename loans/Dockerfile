#start with base image containing  java runtime
FROM openjdk:17-slim as build

#Information around who maintains the image
MAINTAINER anpgn.com

#Add the applications jar to container
COPY build/libs/loans-0.0.1-SNAPSHOT.jar  loans-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java","-jar","/loans-0.0.1-SNAPSHOT.jar"]