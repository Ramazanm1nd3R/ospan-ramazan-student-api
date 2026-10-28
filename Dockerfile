# Multi-stage build of the Student API (Ramazan Ospan, IT2-2310)

# ---- Stage 1: build the JAR with the Gradle wrapper on a full JDK ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
# build files first: the dependency layer is cached until build.gradle changes
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN ./gradlew --no-daemon dependencies > /dev/null
COPY src src
RUN ./gradlew --no-daemon bootJar

# ---- Stage 2: small runtime image with only a JRE and the JAR ----
FROM eclipse-temurin:21-jre-alpine
LABEL org.opencontainers.image.title="ospan-ramazan-student-api" \
      org.opencontainers.image.authors="Ramazan Ospan <ospanr4@gmail.com>" \
      kz.ospan.student="Ramazan Ospan, IT2-2310"
RUN apk add --no-cache curl \
 && addgroup -S app && adduser -S ramazan -G app
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
USER ramazan
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --start-period=40s --retries=5 \
  CMD curl -fs http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
