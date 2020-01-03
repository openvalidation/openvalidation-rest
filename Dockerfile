FROM maven:3.6.0-jdk-8 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:8-jdk-alpine
COPY --from=build /usr/src/app/target/ov-rest.jar /ov-rest.jar

EXPOSE 80

ENTRYPOINT ["java","-jar","/ov-rest.jar","--server.port=80"]
