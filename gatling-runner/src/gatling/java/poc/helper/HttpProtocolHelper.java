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
    private final String configPath = FileConfig.getLocalGatlingConfigPath();
    private final Config config = ConfigFactory.load(configPath);


    public HttpProtocolBuilder createProtocolBuilder() {
        String baseURL = config.getString("baseURL");
        logger.info("BASEURL OF SIMULATION: " + baseURL);

        if (config.hasPath("scenario.params.requestParams")) {
            String requestParamsPath = config.getString("scenario.params.requestParams");
            logger.info("Using REQUEST PARAMS: " + requestParamsPath);

            Map<String, String> headers = this.getHeaders(requestParamsPath);
            return http.baseUrl(baseURL).headers(headers);
        }
        return http.baseUrl(baseURL);
    }

    @SuppressWarnings("unchecked")
    private Map<String,String> getHeaders(String requestParamsPath) {
        Map<String,Object> requestParams = jsonFile(requestParamsPath).readRecords().get(0);

        Map2<?,?> headersAsScala = (scala.collection.immutable.Map.Map2<?,?>) requestParams.get("headers");
        Map<String, String> headersAsJava = (Map<String,String>) CollectionConverters.MapHasAsJava(headersAsScala).asJava();
        return headersAsJava;
    }
}
