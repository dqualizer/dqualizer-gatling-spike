package poc.helper;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CheckBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import poc.config.FileConfig;
import poc.exception.UnknownTypeException;
import scala.jdk.CollectionConverters;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class RequestHelper {
    private final Logger logger = Logger.getLogger(RequestHelper.class.getName());
    private final Config action;
    private final Config params;

    public RequestHelper(Config action) {
        this.action = action;
        this.params = action.getConfig("params");
    }

    public ChainBuilder getChainBuilder() {
        Config request = action.getConfig("request");
        HttpRequestActionBuilder actionBuilder = this.getActionBuilder(request);
        return exec(actionBuilder);
    }

    private HttpRequestActionBuilder getActionBuilder(Config requestConfig) {
        String path = requestConfig.getString("path");
        String method = requestConfig.getString("method");

        HttpRequestActionBuilder actionBuilder;

        switch (method) {
            case "GET" -> {
                actionBuilder = http(path).get(path);
            }
            case "POST" -> {
                actionBuilder = http(path).post(path);
            }
            case "PUT" -> {
                actionBuilder = http(path).put(path);
            }
            case "DELETE" -> {
                actionBuilder = http(path).delete(path);
            }
            default -> throw new UnknownTypeException(method);
        }

        if (params.hasPath("queryParams")) {
            Map<String, Object> queryParams = this.getQueryParams();
            actionBuilder = actionBuilder.queryParamMap(queryParams);
        }
        if(params.hasPath("requestParams")) {
            Map<String, String> headers = this.getHeaders();
            actionBuilder = actionBuilder.headers(headers);
        }

        return actionBuilder.check(this.getCheckBuilder());
    }

    private Map<String, Object> getQueryParams() {
        String queryParamsPath = params.getString("queryParams");
        logger.info("Using QUERY PARAMS: " + queryParamsPath);

        List<Map<String,Object>> mapList = jsonFile(queryParamsPath).readRecords();
        // There should be only one object inside the list
        Map<String, Object> queryParams = mapList.get(0);
        return queryParams;
    }

    private CheckBuilder getCheckBuilder() {
        Config checks = action.getConfig("checks");

        if(checks.hasPath("statusCodes")) {
            List<Integer> expectedStatusCodes = checks.getIntList("statusCodes");
            return status().in(expectedStatusCodes);
        }

        // Default check
        return status().exists();
    }

    private Map<String,String> getHeaders() {
        String requestParamsPath = params.getString("requestParams");
        logger.info("Using REQUEST PARAMS: " + requestParamsPath);

        Map<String,Object> requestParams = jsonFile(requestParamsPath).readRecords().get(0);

        scala.collection.immutable.Map.Map2<?,?> headersAsScala = (scala.collection.immutable.Map.Map2<?,?>) requestParams.get("headers");
        Map<String, String> headersAsJava = (Map<String,String>) CollectionConverters.MapHasAsJava(headersAsScala).asJava();
        return headersAsJava;
    }
}
