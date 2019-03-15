# Builder
FROM maven:3.6.0-jdk-8-alpine AS builder

WORKDIR /build
COPY ./ /build
RUN mvn package

# Runner
FROM openjdk:8-jre-alpine
EXPOSE 7000
COPY --from=builder /build/Core/target/*jar-with-dependencies.jar /app/app.jar
CMD exec java $JAVA_OPTS -jar /app/app.jar

