package poc.simulation;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.*;
import poc.config.FileConfig;
import poc.helper.HttpProtocolHelper;
import poc.helper.InjectionHelper;
import poc.helper.ScenarioHelper;

import java.nio.file.Path;
import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.global;

public class DqSimulation extends Simulation {

    private final Logger logger = Logger.getLogger(DqSimulation.class.getName());
    private final Config config = ConfigFactory.load(FileConfig.getLocalGatlingConfigPath());

    private final ScenarioHelper scenarioHelper = new ScenarioHelper(config);
    private final InjectionHelper injectionHelper = new InjectionHelper(config);
    private final HttpProtocolHelper httpProtocolHelper = new HttpProtocolHelper();

    private PopulationBuilder createPopulationBuilder() {
        ScenarioBuilder scenario = scenarioHelper.getScenarioBuilder();
        PopulationBuilder population = injectionHelper.getPopulationBuilder(scenario);
        return population;
    }

    private ProtocolBuilder createProtocolBuilder() {
        ProtocolBuilder protocol = httpProtocolHelper.createProtocolBuilder(config);
        return protocol;
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

    // TODO: Anscheinend werden bisher die Lasttests parallel ausgeführt
    //  Für eine sequentielle Ausführung muss man das glaub ich hier im setUp() machen
    //  Aber hey, der aktuelle Code ist sicher in Zukunft noch brauchbar
    {
        setUp(this.createPopulationBuilder())
                .protocols(this.createProtocolBuilder())
                .assertions(global().successfulRequests().percent().is(100.0)); //Validate whether all checks have passed
    }
}
