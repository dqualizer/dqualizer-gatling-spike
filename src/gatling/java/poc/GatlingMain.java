package poc;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import poc.adapter.GatlingAdaptationService;
import poc.config.FileConfig;

import java.io.*;
import java.util.logging.Logger;

public class GatlingMain {

    private static final Logger logger = Logger.getLogger(GatlingMain.class.getName());
    private static final GatlingAdaptationService adaptationService = new GatlingAdaptationService();
    private static final String gatlingConfigPath = FileConfig.getAbsoluteGatlingConfigPath();

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
        String projectDir = System.getProperty("user.dir");
        ProjectConnection connection = GradleConnector.newConnector()
                .forProjectDirectory(new File(projectDir))
                .connect();

        connection.newBuild().forTasks("gatlingRun")
                .setColorOutput(true)
                .setStandardOutput(System.out)
                .setStandardError(System.out)
                .run();
        connection.close();
    }
}
