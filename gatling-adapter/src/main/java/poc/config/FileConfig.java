package poc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import poc.exception.UnknownTypeException;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Slf4j
public class FileConfig {

    // Project, where the created gatling config file should be created
    @Value("${gatling.config.project:gatling-runner}")
    private String projectDirectory;
    private static final String gatlingConfigPath = "config/dq-gatling.conf";
    private static final String constantsPath = "constant/constants.json";

    private String getResourcePath(String resource) {
        Path resourceDirectory = Paths.get(resource).toAbsolutePath().normalize();
        return resourceDirectory.toString();
    }

    /**
     * @return Absolute path, where the gatling config file should be created
     */
    public String getGatlingConfigPath() {
        String resourceDirectory;

        if(projectDirectory.equals("gatling-adapter")) resourceDirectory = "gatling-adapter/src/main/resources/";
        else if(projectDirectory.equals("gatling-runner")) resourceDirectory = "gatling-runner/src/gatling/resources/";
        else throw new UnknownTypeException(projectDirectory);

        return getResourcePath(resourceDirectory + gatlingConfigPath);
    }

    /**
     * Return the path to the file with defined values for load test constants, like HIGH or SLOW
     *
     * @return Absolute path of the constants file
     */
    public String getConstantsPath() {
        String resourceDirectory = "gatling-adapter/src/main/resources/";
        return getResourcePath(resourceDirectory + constantsPath);
    }
}
