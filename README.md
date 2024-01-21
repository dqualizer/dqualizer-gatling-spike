Gatling-Java-POC
=============================================

There is a dependency between all three modules, which is why the modules should be executed in the following order:

gatling-adapter > gatling-runner > gatling-exporter

## gatling-adapter

The gatling-adapter reads a RQA-Configuration and adapt it to a dqualizer gatling-configuration.
The created gatling-configuration will be written either into the _gatling-adapter_ project or the _gatling-runner_ project.
You can configure the project via the environmental variable `GATLING_CONFIG_MODULE`.

---
## gatling-runner

The gatling-runner provides a _Simulation_ template for gatling load tests. 
To run load tests, a dqualizer gatling-configuration has to be provided **before building the project**. 

An example for a dqualizer gatling configuration can be found [here](src/gatling/resources/poc/gatling-example.conf).

Gatling HTML-reports will be stored in the folder [results](results).

---
## gatling-exporter

The gatling-exporter uses the _simulation.log_ files from the [results](results) folder to create metrics.
These metrics will be exported to an OpenTelemetry-Collector via HTTP.
You can configure the host for the collector via the environmental variable `OTEL_HOST`.

---
## Docker

When running in Docker, it is important to pass specific files from one module to another.
You can use the _docker-shared_ folder for this.

The gatling-adapter creates a _dq-gatling.conf_, which has to be passed to the gatling-runner.
The `wait_for_file.sh` script will wait until the configuration exists, before starting the jar.
The gatling-runner creates simulation results, which have to be passed to the gatling-exporter.

Additionally, the _docker-config_ folder contains configurations for the OpenTelemetry-Collector as well as Grafana.

All created Metrics will be stored inside a InfluxDB (v2) and can be viewed in Grafana.
