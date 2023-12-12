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

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ScenarioHelper {
    private final Logger logger = Logger.getLogger(ScenarioHelper.class.getName());
    private final String context;
    private final int thinkTime;

    public ScenarioHelper(Config config) {
        this.context = config.getString("context");
        this.thinkTime = config.getConfig("technicalConstants").getInt("thinkTime");
        logger.info("Using THINK TIME: " + thinkTime + " seconds");
    }

    public ScenarioBuilder getScenarioBuilder(Config loadTest, int counter) {
        String scenarioName = context + "-" + counter;
        ScenarioBuilder scenarioBuilder = scenario(scenarioName);
        ChainBuilder requestChain = null;

        List<? extends Config> actions = loadTest.getConfigList("actions");

        if(actions.size() < 1) throw new IllegalStateException("Action list does not contain actions");
        logger.info("Found " + actions.size() + " request actions");

        for(Config action : actions) {
            RequestHelper requestHelper = new RequestHelper(action);
            ChainBuilder request = requestHelper.getChainBuilder();
            if(requestChain == null) requestChain = exec(request);
            else requestChain = requestChain.exec(request);

            Config params = action.getConfig("params");
            ParamsHelper paramsHelper = new ParamsHelper(params);

            if(params.hasPath("payload")) {
                FeederBuilder<?> feeder = paramsHelper.getFeederBuilder("payload").random();
                scenarioBuilder = scenarioBuilder.feed(feeder);
            }
            if(params.hasPath("pathVariables")) {
                FeederBuilder<?> feeder = paramsHelper.getFeederBuilder("pathVariables").random();
                scenarioBuilder = scenarioBuilder.feed(feeder);
            }
        }

        int repetition = loadTest.getInt("repetition");
        logger.info("Using REPETITION: " + repetition);

        return scenarioBuilder.repeat(repetition).on(requestChain.pause(thinkTime));
    }
}
