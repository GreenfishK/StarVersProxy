FROM maven:3.8.4-amazoncorretto-11 AS build

ADD pom.xml pom.xml
ADD src src

RUN mvn clean compile assembly:single
