FROM gradle:8-alpine AS build

WORKDIR /app
COPY . .

# Build fatJar with gatling and external dependencies
RUN gradle shadowJar

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# Copy the executables from the build stage
COPY --from=build app gatling-runner
RUN chmod +x gatling-runner/scripts/wait_for_file.sh

# Run script, which waits for the gatling config file before running the jar file
ENTRYPOINT ["gatling-runner/scripts/wait_for_file.sh" , "/app/gatling-runner/src/gatling/resources/config/dq-gatling.conf"]
