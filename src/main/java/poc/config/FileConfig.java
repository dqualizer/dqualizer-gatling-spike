package poc.config;

public class FileConfig {

    private static final String gatlingConfigName = "dq-gatling.conf";

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
        // TODO This is not working... Even though the file is found, the config is missing
        //return getResourcePath() + "poc/gatling-simple.conf";

        return "poc/gatling-simple.conf";
        //return getResourcePath() + gatlingConfigName;
    }

    public static String getConstantsPath() {
        return getResourcePath() + "constant/constants.json";
    }
}
