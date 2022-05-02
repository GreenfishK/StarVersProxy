FROM maven:3.8.4-amazoncorretto-11 AS build

ADD pom.xml /opt/proxy/pom.xml
ADD src /opt/proxy/src

RUN mvn -f /opt/proxy/pom.xml clean compile assembly:single
