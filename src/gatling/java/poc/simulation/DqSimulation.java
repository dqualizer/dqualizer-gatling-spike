package poc.simulation;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.*;
import poc.config.FileConfig;
import poc.helper.HttpProtocolHelper;
import poc.helper.InjectionHelper;
import poc.helper.ScenarioHelper;

import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.global;

public class DqSimulation extends Simulation {

    private final Logger logger = Logger.getLogger(DqSimulation.class.getName());
    private final String configPath = FileConfig.getGatlingConfigPath();
    private final Config config = ConfigFactory.load(configPath);

    private final ScenarioHelper scenarioHelper = new ScenarioHelper();
    private final InjectionHelper injectionHelper = new InjectionHelper();
    private final HttpProtocolHelper httpProtocolHelper = new HttpProtocolHelper();

    private PopulationBuilder createPopulationBuilder() {
        ScenarioBuilder scenario = scenarioHelper.getScenarioBuilder();
        PopulationBuilder population = injectionHelper.getPopulationBuilder(scenario);
        return population;
    }

    @Override
    public void before() {
        logger.info("SIMULATION IS ABOUT TO START");
        logger.info("CONFIGURATION: " + config);
    }

    @Override
    public void after() {
        logger.info("SIMULATION IS FINISHED");
    }

    {
        setUp(this.createPopulationBuilder())
                .protocols(httpProtocolHelper.createProtocolBuilder())
                .assertions(global().successfulRequests().percent().is(100.0)); //Validate whether all checks have passed
    }
}
