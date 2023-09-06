package poc.adapter;

import poc.dqlang.loadtest.*;
import poc.util.ConfigModelCreator;
import poc.util.MultiLineFileWriter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class GatlingAdaptationService {

    private final Logger logger = Logger.getLogger(GatlingAdaptationService.class.getName());
    private final GatlingAdapter adapter;

    public GatlingAdaptationService() {
        this.adapter = new GatlingAdapter();
    }

    public void start(String configPath) throws IOException {
        LoadTestConfig config = ConfigModelCreator.create();
        logger.info("LOADTEST CONFIG WAS CREATED");

        List<String> gatlingConfigString = adapter.adapt(config);
        logger.info("LOADTEST CONFIG WAS ADAPTED");

        MultiLineFileWriter.write(gatlingConfigString, configPath);
    }
}
