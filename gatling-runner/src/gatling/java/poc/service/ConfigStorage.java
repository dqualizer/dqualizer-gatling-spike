package poc.service;

import poc.dqlang.gatling.GatlingConfiguration;

public class ConfigStorage {

    private static GatlingConfiguration configuration;

    public static GatlingConfiguration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(GatlingConfiguration newConfiguration) {
        configuration = newConfiguration;
    }
}
