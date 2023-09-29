package poc;

import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import poc.config.FileConfig;
import poc.export.MetricExporter;
import poc.simulation.DqSimulation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;

public class RunnerMain {

    private static final Logger logger = Logger.getLogger(RunnerMain.class.getName());
    private static final String gatlingConfigPath = FileConfig.getAbsoluteGatlingConfigPath();
    private static final Path resultsFolderPath = FileConfig.getResultFilePath();

    public static void main(String[] args) {
        logger.info("PATH FOR GATLING CONFIGURATION: " + gatlingConfigPath);
        logger.info("NOTE THAT THE CONFIGURATION FILE MUST ALREADY EXIST DURING COMPILATION");
        runGatling();

        logger.info("STARTING EXPORTING METRICS FROM HERE: " + resultsFolderPath);
        try {
            MetricExporter.export();
        } catch (Exception e) {
            logger.severe("METRIC EXPORT FAILED");
            e.printStackTrace();
        }
    }

    private static void runGatling() {
        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
                .simulationClass(DqSimulation.class.getName());

        Gatling.fromMap(props.build());
    }
}
