Gatling-Java-POC
=============================================

## gatling-adapter

The gatling-adapter should read a RQA-Configuration and adapt it to a dqualizer gatling-configuration.
The created gatling-configuration will be written into the gatling-runner project.

---
## gatling-runner

The gatling-runner provides a Simulation template for gatling load tests. To run load tests, a dqualizer gatling-configuration
has to be provided **before building or running the project**. 

An example for a dqualizer gatling configuration can be found [here](gatling-runner/src/gatling/resources/poc/gatling-example.conf).
