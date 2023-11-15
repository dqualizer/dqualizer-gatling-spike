package poc;

import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import poc.config.FileConfig;
import poc.simulation.DqSimulation;

import java.util.logging.Logger;

public class RunnerMain {

    private static final Logger logger = Logger.getLogger(RunnerMain.class.getName());
    private static final String gatlingConfigPath = FileConfig.getAbsoluteGatlingConfigPath();

    public static void main(String[] args) {
        logger.info("STARTING GATLING RUNNER");
        logger.info("PATH FOR GATLING CONFIGURATION: " + gatlingConfigPath);
        logger.info("NOTE THAT THE CONFIGURATION FILE MUST ALREADY EXIST DURING COMPILATION");
        runGatling();
    }

    private static void runGatling() {
        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
                .simulationClass(DqSimulation.class.getName());

        Gatling.fromMap(props.build());
    }
}
