package poc.helper;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import poc.config.FileConfig;
import poc.simulation.DqSimulation;
import scala.jdk.CollectionConverters;

import java.util.Map;
import scala.collection.immutable.Map.Map2;
import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.jsonFile;
import static io.gatling.javaapi.http.HttpDsl.http;

public class HttpProtocolHelper {

    private final Logger logger = Logger.getLogger(DqSimulation.class.getName());

    public HttpProtocolBuilder createProtocolBuilder(Config config) {
        String baseURL = config.getString("baseURL");
        logger.info("BASEURL OF SIMULATION: " + baseURL);

        return http.baseUrl(baseURL);
    }
}
