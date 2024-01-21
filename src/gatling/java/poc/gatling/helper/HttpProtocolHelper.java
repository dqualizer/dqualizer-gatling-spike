package poc.gatling.helper;

import io.gatling.javaapi.http.HttpProtocolBuilder;
import poc.dqlang.gatling.GatlingConfiguration;
import poc.gatling.simulation.DqSimulation;

import java.util.logging.Logger;

import static io.gatling.javaapi.http.HttpDsl.http;
import static poc.util.CustomLogger.printLog;

public class HttpProtocolHelper {
    private final GatlingConfiguration config;

    public HttpProtocolHelper(GatlingConfiguration config) {
        this.config = config;
    }

    public HttpProtocolBuilder createProtocolBuilder() {
        String baseURL = config.getBaseURL();
        printLog(this.getClass(), "BASEURL OF SIMULATION: " + baseURL);

        return http.baseUrl(baseURL);
    }
}
