package poc.helper;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CheckBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import poc.config.FileConfig;
import poc.exception.UnknownTypeException;
import poc.injector.ClosedInjection;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ScenarioHelper {
    private final Logger logger = Logger.getLogger(ScenarioHelper.class.getName());

    private final Config scenario;
    private final Config params;

    public ScenarioHelper() {
        String configPath = FileConfig.getLocalGatlingConfigPath();
        this.scenario = ConfigFactory.load(configPath).getConfig("scenario");
        this.params = scenario.getConfig("params");
    }

    public ScenarioBuilder getScenarioBuilder() {
        String name = scenario.getString("name");
        String method = scenario.getString("request.method");
        String path = scenario.getString("request.path");
        ScenarioBuilder scenarioBuilder = scenario(name);

        if(params.hasPath("payload")) {
            FeederBuilder<?> feeder = this.getFeederBuilder("payload").random();
            scenarioBuilder = scenarioBuilder.feed(feeder);
        }
        if(params.hasPath("pathVariables")) {
            FeederBuilder<?> feeder = this.getFeederBuilder("pathVariables").random();
            scenarioBuilder = scenarioBuilder.feed(feeder);
        }

        HttpRequestActionBuilder actionBuilder = this.getActionBuilder(method, path);
        ChainBuilder requestChain = exec(actionBuilder);
        int repetition = scenario.getInt("repetition");
        int thinkTime = this.getThinkTime();

        logger.info("Using REPETITION: " + repetition);
        logger.info("Using THINK TIME: " + thinkTime + " seconds");

        return scenarioBuilder.repeat(repetition).on(exec(requestChain).pause(thinkTime));
    }

    private HttpRequestActionBuilder getActionBuilder(String method, String path) {
        String name = "My-Request-Name";    //TODO Customizable?
        HttpRequestActionBuilder actionBuilder;

        switch (method) {
            case "GET" -> {
                actionBuilder = http(name).get(path).check(this.getCheckBuilder());
            }
            case "POST" -> {
                actionBuilder = http(name).post(path).check(this.getCheckBuilder());
            }
            case "PUT" -> {
                actionBuilder = http(name).put(path).check(this.getCheckBuilder());
            }
            case "DELETE" -> {
                actionBuilder = http(name).delete(path).check(this.getCheckBuilder());
            }
            default -> throw new UnknownTypeException(method);
        }
        if (params.hasPath("queryParams")) {
            Map<String, Object> queryParams = this.getQueryParams();
            return actionBuilder.queryParamMap(queryParams);
        }
        return actionBuilder;
    }

    // Currently, only json & csv are supported
    private FeederBuilder<?> getFeederBuilder(String feeder) {
        String feederPath = params.getString(feeder);
        logger.info("Using FEEDER: " + feederPath);

        if(feederPath.endsWith(".json")) return jsonFile(feederPath);
        else if(feederPath.endsWith(".json.zip")) return jsonFile(feederPath).unzip();
        else if(feederPath.endsWith(".csv")) return csv(feederPath);
        else if(feederPath.endsWith(".csv.zip")) return csv(feederPath).unzip();
        else throw new UnknownTypeException(feederPath);
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
        Config checks = scenario.getConfig("checks");

        if(checks.hasPath("statusCodes")) {
            List<Integer> expectedStatusCodes = checks.getIntList("statusCodes");
            return status().in(expectedStatusCodes);
        }

        // Default check
        return status().exists();
    }

    private int getThinkTime() {
        String configPath = FileConfig.getLocalGatlingConfigPath();
        Config technicalConfig = ConfigFactory.load(configPath).getConfig("technicalConstants");
        int thinkTime = technicalConfig.getInt("thinkTime");

        return thinkTime;
    }
}
