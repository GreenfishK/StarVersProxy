FROM maven:3.8.4-amazoncorretto-11 AS build

COPY pom.xml /opt/proxy/pom.xml
COPY src /opt/proxy/src

RUN mvn -f /opt/proxy/pom.xml clean compile assembly:single
