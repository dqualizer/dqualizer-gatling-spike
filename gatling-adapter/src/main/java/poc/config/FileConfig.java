package poc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

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

    public static String getAbsoluteGatlingConfigPath() {
        return getResourcePath(gatlingConfigPath);
    }

    public static String getConstantsPath() {
        return getResourcePath(constantsPath);
    }
}
