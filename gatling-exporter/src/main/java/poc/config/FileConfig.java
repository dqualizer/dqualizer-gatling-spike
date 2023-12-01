package poc.config;

import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileConfig {

    /**
     * Returns the absolute path of the results folder, which contains simulation results
     *
     * @return Absolute path of results folder
     */
    public Path getResultFilePath() {
        String path = System.getProperty("user.dir") + "/results";
        return Paths.get(path);
    }
}
