package poc.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileConfig {

    private static final String resourceDirectory = "gatling-runner/src/gatling/resources/";
    private static final String gatlingConfigPath = "config/dq-gatling.conf";

    private static String getResourcePath(String resource) {
        Path resourceDirectory = Paths.get(resource).toAbsolutePath().normalize();
        return resourceDirectory.toString();
    }

    /**
     * Returns the absolute path of the gatling config file, which can be used for debugging or logging
     * This path should not be used inside the Simulation class
     *
     * @return Absolute path of gatling config file
     */
    public static String getAbsoluteGatlingConfigPath() {
        String resource = resourceDirectory + gatlingConfigPath;
        return getResourcePath(resource);
    }

    /**
     * Returns the local path of the gatling config file, which is relative to <root-dir>/src/gatling/resources
     * Only this path should be used inside the Simulation class
     *
     * @return Local path of gatling config file
     */
    public static String getLocalGatlingConfigPath() {
        return gatlingConfigPath;
    }
}
