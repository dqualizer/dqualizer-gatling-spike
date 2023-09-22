package poc.config;

import java.io.File;

public class FileConfig {

    private static final String resourceDirectory = "gatling-runner/src/gatling/resources/";
    private static final String gatlingConfigPath = "config/dq-gatling.conf";

    private static String getResourcePath(String resource) {
        File resourceFile = new File(resource);
        String path = resourceFile.getAbsolutePath();

        return path;
    }

    public static String getAbsoluteGatlingConfigPath() {
        String resource = resourceDirectory + gatlingConfigPath;
        return getResourcePath(resource);
    }

    public static String getLocalGatlingConfigPath() {
        return gatlingConfigPath;
    }
}
