FROM gradle:8-alpine AS build

WORKDIR /app
COPY . .

# Build fatJar with gatling and external dependencies
RUN gradle shadowJar

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# Copy the executables from the build stage
COPY --from=build app gatling-runner

# Run the jar file
ENTRYPOINT ["java", "-jar", "gatling-runner/build/libs/gatling-runner-all.jar"]
