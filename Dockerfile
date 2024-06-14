FROM jelastic/maven:3.9.5-openjdk-21 AS build-stage
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline


# Package stage
COPY ./src ./src
RUN mvn clean install -Dmaven.test.skip=true

FROM openjdk:21-jdk AS production-stage
WORKDIR /app
COPY --from=build-stage /app/target/*.jar apilog.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/apilog.jar"]