package poc;

import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import poc.config.FileConfig;
import poc.simulation.DqSimulation;

import java.io.*;
import java.util.logging.Logger;

public class RunnerMain {

    private static final Logger logger = Logger.getLogger(RunnerMain.class.getName());
    private static final String gatlingConfigPath = FileConfig.getAbsoluteGatlingConfigPath();

    public static void main(String[] args) {
        logger.info("PATH FOR GATLING CONFIGURATION: " + gatlingConfigPath);
        runGatling();
    }

    private static void runGatling() {
//        String projectDir = System.getProperty("user.dir");
//        ProjectConnection connection = GradleConnector.newConnector()
//                .forProjectDirectory(new File(projectDir))
//                .connect();
//
//        connection.newBuild().forTasks("gatlingRun")
//                .setColorOutput(true)
//                .setStandardOutput(System.out)
//                .setStandardError(System.out)
//                .run();
//        connection.close();

        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
                .simulationClass(DqSimulation.class.getName());

        Gatling.fromMap(props.build());
    }
}
