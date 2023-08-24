package poc.config;

public class FileConfig {

    private static final String gatlingConfigName = "dq-gatling.conf";

    private static String getResourcePath() {
        return FileConfig.class.getClassLoader()
                .getResource(".")
                .getFile()
                .substring(1);
    }

    public static String getGatlingConfigPath() {
        return "poc/gatling-simple.conf";
        //return getResourcePath() + gatlingConfigName;
    }

    public static String getRessource(String localPath) {
        return getResourcePath() + localPath;
    }
}
