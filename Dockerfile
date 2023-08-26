FROM gradle:7.5.1-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:20-slim

EXPOSE 8080

RUN mkdir /app

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/soapws.jar /app/soapws.jar

ENTRYPOINT ["java","-jar","soapws.jar"]