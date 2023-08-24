package poc.adapter;

import poc.dqlang.LoadTestConfig;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


public class GatlingAdaptationService {

    private final Logger logger = Logger.getLogger(GatlingAdaptationService.class.getName());
    private GatlingAdapter adapter;
    private MultiLineFileWriter writer;

    public GatlingAdaptationService() {
        this.adapter = new GatlingAdapter();
        this.writer = new MultiLineFileWriter();
    }

    private LoadTestConfig createConfigModel() {
        return null;
    }

    public void start(String configPath) throws IOException {
        if(true) return;
        LoadTestConfig config = this.createConfigModel();
        logger.info("LOADTEST CONFIG WAS CREATED");

        List<String> gatlingConfigString = adapter.adapt(config);
        logger.info("LOADTEST CONFIG WAS ADAPTED");

        writer.write(gatlingConfigString, configPath);
    }
}
