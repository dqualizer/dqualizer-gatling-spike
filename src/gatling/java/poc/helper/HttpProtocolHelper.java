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
    private final String configPath = FileConfig.getGatlingConfigPath();
    private final Config config = ConfigFactory.load(configPath);

    @SuppressWarnings("unchecked")
    public HttpProtocolBuilder createProtocolBuilder() {
        String baseURL = config.getString("baseURL");
        String requestParamsPath = config.getString("scenario.params.requestParams");
        Map<String,Object> requestParams = jsonFile(requestParamsPath).readRecords().get(0);

        Map2<?,?> headersAsScala = (scala.collection.immutable.Map.Map2<?,?>) requestParams.get("headers");
        Map<String, String> headersAsJava = (Map<String,String>) CollectionConverters.MapHasAsJava(headersAsScala).asJava();

        return http.baseUrl(baseURL).headers(headersAsJava);
    }
}
