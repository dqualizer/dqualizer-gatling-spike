package poc.gatling.helper;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import poc.dqlang.gatling.GatlingConfiguration;
import poc.dqlang.gatling.GatlingLoadTest;
import poc.dqlang.gatling.action.GatlingAction;
import poc.dqlang.gatling.action.GatlingParams;
import poc.exception.UnknownTypeException;

import static io.gatling.javaapi.core.CoreDsl.*;
import static poc.util.CustomLogger.printLog;

import java.util.List;
import java.util.logging.Logger;

public class ScenarioHelper {
    private final String context;
    private final int thinkTime;

    public ScenarioHelper(GatlingConfiguration config) {
        this.context = config.getContext();
        this.thinkTime = config.getTechnicalConstants().getThinkTime();
        printLog(this.getClass(), "Using THINK TIME: " + thinkTime + " seconds");
    }

    public ScenarioBuilder getScenarioBuilder(GatlingLoadTest loadTest, int counter) {
        String scenarioName = context + "-" + counter;
        ScenarioBuilder scenarioBuilder = scenario(scenarioName);
        ChainBuilder requestChain = null;

        List<GatlingAction> actions = loadTest.getActions();

        if(actions.size() < 1) throw new IllegalStateException("Action list does not contain actions");
        printLog(this.getClass(), "Found " + actions.size() + " request action(s)");

        for(GatlingAction action : actions) {
            RequestHelper requestHelper = new RequestHelper(action);
            ChainBuilder request = requestHelper.getChainBuilder();
            if(requestChain == null) requestChain = exec(request);
            else requestChain = requestChain.exec(request);

            GatlingParams params = action.getParams();

            if(params.getPayload() != null) {
                FeederBuilder<?> feeder = this.getFeederBuilder(params.getPayload()).random();
                scenarioBuilder = scenarioBuilder.feed(feeder);
            }
            if(params.getPathVariables() != null) {
                FeederBuilder<?> feeder = this.getFeederBuilder(params.getPathVariables()).random();
                scenarioBuilder = scenarioBuilder.feed(feeder);
            }
        }

        int repetition = loadTest.getRepetition();
        printLog(this.getClass(), "Using REPETITION: " + repetition);

        return scenarioBuilder.repeat(repetition).on(requestChain.pause(thinkTime));
    }

    // Currently, only json & csv are supported
    private FeederBuilder<?> getFeederBuilder(String feederPath) {
        printLog(this.getClass(), "Using FEEDER: " + feederPath);

        if(feederPath.endsWith(".json")) return jsonFile(feederPath);
        else if(feederPath.endsWith(".json.zip")) return jsonFile(feederPath).unzip();
        else if(feederPath.endsWith(".csv")) return csv(feederPath);
        else if(feederPath.endsWith(".csv.zip")) return csv(feederPath).unzip();
        else throw new UnknownTypeException(feederPath);
    }
}
