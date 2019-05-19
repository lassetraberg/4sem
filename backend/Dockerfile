# Builder
FROM maven:3.6.0-jdk-8-alpine AS builder

WORKDIR /build
COPY ./ /build
RUN mvn clean package

# Runner
FROM openjdk:8-jre-alpine
EXPOSE 7000
COPY --from=builder /build/Core/target/Core-1.0-SNAPSHOT.jar /app/app.jar
CMD exec java -jar /app/app.jar

