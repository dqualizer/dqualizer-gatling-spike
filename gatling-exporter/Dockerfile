FROM gradle:8-alpine AS build

WORKDIR /app
COPY . .

# Build jar
RUN gradle assemble

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# Copy the executables from the build stage
COPY --from=build app gatling-exporter

# Run the jar file
ENTRYPOINT ["java", "-jar", "gatling-exporter/build/libs/gatling-exporter.jar"]
