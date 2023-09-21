### ----------- Builder Base Image ----------- ###
FROM gradle:8-alpine AS build

# Set the working directory to /app
WORKDIR /app
COPY . .

RUN gradle assemble

FROM eclipse-temurin:17-jre-alpine

# Set the working directory to /app
WORKDIR /app
# Copy the executables from the build stages
COPY --from=build app/gatling-adapter /app/gatling-adapter

# Run the jar file
#ENTRYPOINT ["tree", "/app/gatling-adapter"]
ENTRYPOINT ["java", "-jar", "gatling-adapter/build/libs/gatling-adapter.jar"]
