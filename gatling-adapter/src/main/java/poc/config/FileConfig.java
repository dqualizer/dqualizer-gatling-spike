package poc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@Slf4j
public class FileConfig {

    private static final String resourceDirectory = "gatling-adapter/src/main/resources/";
    private static final String gatlingConfigPath = resourceDirectory + "gatling/dq-gatling.conf";
    private static final String constantsPath = resourceDirectory + "constant/constants.json";

    private static String getResourcePath(String resource) {
        File resourceFile = new File(resource);
        String path = resourceFile.getAbsolutePath();

        return path;
    }

    /**
     * @return Absolute path, where the gatling config file should be created
     */
    public static String getGatlingConfigPath() {
        return getResourcePath(gatlingConfigPath);
    }

    /**
     * Return the path to the file with defined values for load test constants, like HIGH or SLOW
     *
     * @return Absolute path of the constants file
     */
    public static String getConstantsPath() {
        return getResourcePath(constantsPath);
    }
}
