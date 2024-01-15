package poc.service;

import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import poc.adapter.GatlingAdapter;
import poc.config.FileConfig;
import poc.dqlang.gatling.GatlingConfiguration;
import poc.dqlang.loadtest.LoadTestConfiguration;
import poc.export.MetricExporter;
import poc.gatling.simulation.DqSimulation;
import poc.util.ConfigModelCreator;

import java.nio.file.Path;

@Component
@Slf4j
public class AdaptationManager {

    @Autowired
    private GatlingAdapter adapter;
    @Autowired
    private MetricExporter exporter;

    @EventListener(ApplicationReadyEvent.class) // TODO Remove EventListener
    public void start()  {
        try { this.adapt(); }
        catch (Exception e) {
            log.error("ADAPTATION FAILED: " + e.getMessage());
            e.printStackTrace();
        }

        try { this.runGatling(); }
        catch (Exception e) {
            log.error("GATLING FAILED: " + e.getMessage());
            e.printStackTrace();
        }

        try { exporter.export(); }
        catch (Exception e) {
            log.error("EXPORT FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adapt() {
        // TODO Remove ConfigModelCreator
        LoadTestConfiguration configModel = ConfigModelCreator.create();
        log.info("LOAD TEST CONFIG MODEL WAS CREATED");

        log.info("ADAPTING LOAD TEST");
        GatlingConfiguration configuration = adapter.adapt(configModel);
        log.info("LOAD TEST CONFIG WAS ADAPTED");

        ConfigStorage.setConfiguration(configuration);
        log.info("GATLING CONFIG WAS WRITTEN SUCCESSFULLY");
    }

    private void runGatling() {
        log.info("RUNNING GATLING");
        Path resultsPath = FileConfig.getResultFilePath();
        log.info("RESULTS WILL BE WRITTEN HERE: " + resultsPath);

        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder().simulationClass(DqSimulation.class.getName());
        Gatling.fromMap(props.build());

        // TODO For some reason, the logs are not shown after Gatling is finished
    }
}
