package poc.gatling.helper;

import io.gatling.javaapi.http.HttpProtocolBuilder;
import poc.dqlang.gatling.GatlingConfiguration;
import poc.gatling.simulation.DqSimulation;

import java.util.logging.Logger;

import static io.gatling.javaapi.http.HttpDsl.http;

public class HttpProtocolHelper {

    private final Logger logger = Logger.getLogger(DqSimulation.class.getName());
    private final GatlingConfiguration config;

    public HttpProtocolHelper(GatlingConfiguration config) {
        this.config = config;
    }

    public HttpProtocolBuilder createProtocolBuilder() {
        String baseURL = config.getBaseURL();
        logger.info("BASEURL OF SIMULATION: " + baseURL);

        return http.baseUrl(baseURL);
    }
}
