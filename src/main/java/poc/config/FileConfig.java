package poc.config;

public class FileConfig {

    private static final String gatlingConfigPath = "poc/dq-gatling.conf";
    private static final String constantsPath = "constant/constants.json";

    private static String getResourcePath() {
        String resourceClassPath = FileConfig.class.getClassLoader()
                .getResource(".")
                .getFile()
                .substring(1);
        // For some reason files are build in /build/resources instead of /build/classes/java
        String resourcePath = resourceClassPath.replace("/build/classes/java/", "/build/resources/");
        return resourcePath;
    }

    public static String getGatlingConfigPath() {
        return getResourcePath() + gatlingConfigPath;
    }

    public static String getConstantsPath() {
        return getResourcePath() + constantsPath;
    }
}
