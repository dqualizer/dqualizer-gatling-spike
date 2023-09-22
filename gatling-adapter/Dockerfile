FROM gradle:8-alpine AS build

WORKDIR /app
COPY . .

# Build jar
RUN gradle assemble

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# Copy the executables from the build stages
COPY --from=build app gatling-adapter

# Run the jar file
ENTRYPOINT ["java", "-jar", "gatling-adapter/build/libs/gatling-adapter.jar"]
