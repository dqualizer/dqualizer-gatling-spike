package poc.gatling.helper;

import com.typesafe.config.Config;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CheckBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import poc.dqlang.gatling.action.GatlingAction;
import poc.dqlang.gatling.action.GatlingChecks;
import poc.dqlang.gatling.action.GatlingParams;
import poc.dqlang.gatling.action.GatlingRequest;
import poc.exception.UnknownTypeException;
import scala.jdk.CollectionConverters;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class RequestHelper {
    private final Logger logger = Logger.getLogger(RequestHelper.class.getName());
    private final GatlingAction action;
    private final GatlingParams params;

    public RequestHelper(GatlingAction action) {
        this.action = action;
        this.params = action.getParams();
    }

    public ChainBuilder getChainBuilder() {
        GatlingRequest request = action.getRequest();
        HttpRequestActionBuilder actionBuilder = this.getActionBuilder(request);
        return exec(actionBuilder);
    }

    private HttpRequestActionBuilder getActionBuilder(GatlingRequest request) {
        String path = request.getPath();
        String method = request.getMethod();

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

        if (params.getQueryParams() != null) {
            Map<String, Object> queryParams = this.getQueryParams();
            actionBuilder = actionBuilder.queryParamMap(queryParams);
        }
        if(params.getRequestParams() != null) {
            Map<String, String> headers = this.getHeaders();
            actionBuilder = actionBuilder.headers(headers);
        }

        return actionBuilder.check(this.getCheckBuilder());
    }

    private Map<String, Object> getQueryParams() {
        String queryParamsPath = params.getQueryParams();
        logger.info("Using QUERY PARAMS: " + queryParamsPath);

        List<Map<String,Object>> mapList = jsonFile(queryParamsPath).readRecords();
        // There should be only one object inside the list
        Map<String, Object> queryParams = mapList.get(0);
        return queryParams;
    }

    private CheckBuilder getCheckBuilder() {
        GatlingChecks checks = action.getChecks();

        if(checks.getStatusCodes() != null) {
            List<Integer> expectedStatusCodes = checks.getStatusCodes().stream().toList();
            return status().in(expectedStatusCodes);
        }

        // Default check
        return status().exists();
    }

    private Map<String,String> getHeaders() {
        String requestParamsPath = params.getRequestParams();
        logger.info("Using REQUEST PARAMS: " + requestParamsPath);

        Map<String,Object> requestParams = jsonFile(requestParamsPath).readRecords().get(0);

        scala.collection.immutable.Map.Map2<?,?> headersAsScala = (scala.collection.immutable.Map.Map2<?,?>) requestParams.get("headers");
        Map<String, String> headersAsJava = (Map<String,String>) CollectionConverters.MapHasAsJava(headersAsScala).asJava();
        return headersAsJava;
    }
}
