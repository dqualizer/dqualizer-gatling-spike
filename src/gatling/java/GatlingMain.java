import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import poc.adapter.GatlingAdaptationService;
import poc.config.FileConfig;
import poc.simulation.DqSimulation;
import scala.collection.mutable.Map;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        // TODO fix
        //Since this is static, but the config will be generated during runtime, an exception is thrown
        Gatling.fromMap(props.build());
    }
}
