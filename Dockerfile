FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon --info

FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]