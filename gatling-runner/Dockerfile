FROM gradle:8-alpine AS build

WORKDIR /app
COPY . .

# Build jar
RUN gradle assemble

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# Copy the executables from the build stage
COPY --from=build app gatling-runner

# Run jar
ENTRYPOINT ["java", "-jar", "gatling-runner/build/libs/gatling-runner.jar"]
