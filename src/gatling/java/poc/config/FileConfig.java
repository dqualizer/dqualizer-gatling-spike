package poc.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileConfig {

    private static final String resourceDirectory = "/src/gatling/resources";
    private static final String constantsPath = "/constant/constants.json";
    private static final String resultsPath = "/results";

    /**
     * Returns the absolute path of the results folder, which contains simulation results
     *
     * @return Absolute path of results folder
     */
    public static Path getResultFilePath() {
        String path = System.getProperty("user.dir") + resultsPath;
        return Paths.get(path);
    }

    /**
     * Return the path to the file with defined values for load test constants, like HIGH or SLOW
     *
     * @return Absolute path of the constants file
     */
    public static String getConstantsPath() {
        return System.getProperty("user.dir") + resourceDirectory + constantsPath;
    }
}
