import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import poc.adapter.GatlingAdaptationService;
import poc.config.FileConfig;
import poc.simulation.DqSimulation;

import java.io.IOException;
import java.util.logging.Logger;

public class GatlingMain {

    private static final Logger logger = Logger.getLogger(GatlingMain.class.getName());
    private static final GatlingAdaptationService adaptationService = new GatlingAdaptationService();
    private static final String gatlingConfigPath = FileConfig.getGatlingConfigPath();

    public static void main(String[] args) {
        logger.info("PATH FOR GATLING CONFIGURATION: " + gatlingConfigPath);

        try {
            adaptationService.start(gatlingConfigPath);
        } catch (IOException e) {
            logger.severe("CONFIGURATION ADAPTATION FAILED");
            throw new RuntimeException(e);
        }

        logger.info("ADAPTATION WAS SUCCESSFUL - STARTING GATLING");
        runGatling();
    }

    private static void runGatling() {
        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
                .simulationClass(DqSimulation.class.getName());

        Gatling.fromMap(props.build());
    }
}
