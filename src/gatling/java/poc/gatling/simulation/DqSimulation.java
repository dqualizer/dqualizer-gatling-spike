package poc.gatling.simulation;

import io.gatling.javaapi.core.*;
import poc.dqlang.gatling.GatlingConfiguration;
import poc.dqlang.gatling.GatlingLoadTest;
import poc.gatling.helper.HttpProtocolHelper;
import poc.gatling.helper.InjectionHelper;
import poc.gatling.helper.ScenarioHelper;
import poc.service.ConfigStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.global;

/**
 * Gatling simulation template of dqualizer
 */
public class DqSimulation extends Simulation {

    private final Logger logger = Logger.getLogger(DqSimulation.class.getName());
    private final GatlingConfiguration config = ConfigStorage.getConfiguration();

    private final HttpProtocolHelper httpProtocolHelper = new HttpProtocolHelper(config);
    private final ScenarioHelper scenarioHelper = new ScenarioHelper(config);
    private final InjectionHelper injectionHelper = new InjectionHelper();

    private PopulationBuilder createPopulationBuilderChain() {
        logger.info( "LOADED CONFIGURATION: " + config);
        List<PopulationBuilder> populations = new LinkedList<>();
        List<GatlingLoadTest> loadTests = config.getLoadTests();
        int testCounter = 1;

        for(GatlingLoadTest loadTest : loadTests) {
            logger.info("SETTING UP LOAD TEST " + testCounter);
            ScenarioBuilder scenario = scenarioHelper.getScenarioBuilder(loadTest, testCounter);
            PopulationBuilder population = injectionHelper.getPopulationBuilder(scenario, loadTest);
            populations.add(population);

            logger.info("SUCCESSFULLY SET UP LOAD TEST " + testCounter);
            testCounter++;
        }
        if(populations.isEmpty()) throw new IllegalStateException("NO LOAD TESTS WERE CREATED");

        // Link all PopulationBuilder via andThen() to execute them sequentially
        PopulationBuilder populationChain = populations.remove(0);
        for (PopulationBuilder population : populations)
            populationChain = populationChain.andThen(population);

        return populationChain;
    }

    private ProtocolBuilder createProtocolBuilder() {
        ProtocolBuilder protocol = httpProtocolHelper.createProtocolBuilder();
        return protocol;
    }

    @Override
    public void before() {
        logger.info("SIMULATION IS ABOUT TO START");
    }

    @Override
    public void after() {
        logger.info("SIMULATION IS FINISHED");
    }

    {
        setUp(this.createPopulationBuilderChain())
                .protocols(this.createProtocolBuilder())
                .assertions(global().successfulRequests().percent().is(100.0)); // Validate whether all checks have passed
    }
}
