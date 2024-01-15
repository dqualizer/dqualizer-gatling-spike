package poc.config;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileConfig {

    private static final String constantsPath = "constant/constants.json";

    private static String getResourcePath(String resource) {
        Path resourceDirectory = Paths.get(resource).toAbsolutePath().normalize();
        return resourceDirectory.toString();
    }

    /**
     * Returns the absolute path of the results folder, which contains simulation results
     *
     * @return Absolute path of results folder
     */
    public static Path getResultFilePath() {
        String path = System.getProperty("user.dir") + "/results";
        return Paths.get(path);
    }

    /**
     * Return the path to the file with defined values for load test constants, like HIGH or SLOW
     *
     * @return Absolute path of the constants file
     */
    public static String getConstantsPath() {
        String resourceDirectory = "gatling-runner/src/gatling/resources/";
        String path = getResourcePath(resourceDirectory + constantsPath);
        return path;
    }
}
