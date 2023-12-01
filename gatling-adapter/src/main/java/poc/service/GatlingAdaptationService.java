package poc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import poc.adapter.GatlingAdapter;
import poc.config.FileConfig;
import poc.dqlang.loadtest.*;
import poc.util.ConfigModelCreator;
import poc.util.MultiLineFileWriter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class GatlingAdaptationService {

    @Autowired
    private GatlingAdapter adapter;
    @Autowired
    private FileConfig fileConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void start()  {
        try { this.adapt(); }
        catch (Exception e) {
            log.error("ADAPTATION FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adapt() throws IOException {
        // TODO Remove ConfigModelCreator
        LoadTestConfiguration configModel = ConfigModelCreator.create();
        log.info("LOAD TEST CONFIG MODEL WAS CREATED");

        List<String> gatlingConfig = adapter.adapt(configModel);
        log.info("LOAD TEST CONFIG WAS ADAPTED");

        String configPath = fileConfig.getGatlingConfigPath();
        log.info("GATLING CONFIG WILL BE WRITTEN HERE: " + configPath);
        MultiLineFileWriter.write(gatlingConfig, configPath);
        log.info("GATLING CONFIG WAS WRITTEN SUCCESSFULLY");
    }
}
