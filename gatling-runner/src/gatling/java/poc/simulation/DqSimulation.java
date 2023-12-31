package poc.simulation;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.*;
import poc.config.FileConfig;
import poc.helper.HttpProtocolHelper;
import poc.helper.InjectionHelper;
import poc.helper.ScenarioHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.global;

public class DqSimulation extends Simulation {

    private final Logger logger = Logger.getLogger(DqSimulation.class.getName());
    private final Config config = ConfigFactory.load(FileConfig.getLocalGatlingConfigPath());

    private final HttpProtocolHelper httpProtocolHelper = new HttpProtocolHelper(config);
    private final ScenarioHelper scenarioHelper = new ScenarioHelper(config);
    private final InjectionHelper injectionHelper = new InjectionHelper();

    private PopulationBuilder createPopulationBuilderChain() {
        logger.config( "LOADED CONFIGURATION: " + config);
        List<PopulationBuilder> populations = new LinkedList<>();
        List<? extends Config> loadTests = config.getConfigList("loadTests");
        int counter = 1;

        for(Config loadTest : loadTests) {
            logger.info("SETTING UP LOAD TEST " + counter);
            ScenarioBuilder scenario = scenarioHelper.getScenarioBuilder(loadTest, counter);
            PopulationBuilder population = injectionHelper.getPopulationBuilder(scenario, loadTest);
            populations.add(population);

            logger.info("SUCCESSFULLY SET UP LOAD TEST " + counter);
            counter++;
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
                .assertions(global().successfulRequests().percent().is(100.0)); //Validate whether all checks have passed
    }
}
