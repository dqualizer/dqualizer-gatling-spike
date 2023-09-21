package poc.config;

import java.io.File;

public class FileConfig {

    //TODO Filepath anpassen ggf.
    private static final String gatlingConfigPath = "poc/dq-gatling.conf";
    private static final String constantsPath = "constant/constants.json";

    private static String getResourcePath(String resource) {
//        URL resourceUrl = FileConfig.class.getClassLoader()
//                .getResource(resource);
//        String resourcePath = resourceUrl.getFile();

        String pathString = "gatling-adapter/src/main/resources/constant/constants.json";
        File resourceFile = new File(pathString);

        String path = resourceFile.getAbsolutePath();

        return path;

//        File resourceFile = new File(resourcePath);
//
//        String path = resourceFile.getPath();
//
//        String absolutePath = null;
//        try {
//            absolutePath = Paths.get(new URL(path).toURI()).toString();
//        } catch (URISyntaxException | MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        return absolutePath;


        // For some reason files are build in /build/resources instead of /build/classes/java
        //String resourcePath = resourceClassPath.getPath();//.replace("/build/classes/java/", "/build/resources/");
        //return resourcePath;
    }

    public static String getAbsoluteGatlingConfigPath() {
        return System.getProperty("user.dir") + "/gatling-runner/src/gatling/resources/" + gatlingConfigPath;
    }

    public static String getLocalGatlingConfigPath() {
        return gatlingConfigPath;
    }

    public static String getConstantsPath() {
        return getResourcePath(constantsPath);
    }
}
