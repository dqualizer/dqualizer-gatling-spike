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
import poc.util.CustomLogger;

import java.nio.file.Path;

import static poc.util.CustomLogger.printError;
import static poc.util.CustomLogger.printLog;

@Component
public class AdaptationManager {

    @Autowired
    private GatlingAdapter adapter;
    @Autowired
    private MetricExporter exporter;

    @EventListener(ApplicationReadyEvent.class) // TODO Remove EventListener
    public void start()  {
        try { this.adapt(); }
        catch (Exception e) {
            printError(this.getClass(), "ADAPTATION FAILED: " + e.getMessage());
            e.printStackTrace();
        }

        try { this.runGatling(); }
        catch (Exception e) {
            printError(this.getClass(), "GATLING FAILED: " + e.getMessage());
            e.printStackTrace();
        }

        try { exporter.export(); }
        catch (Exception e) {
            printError(this.getClass(), "EXPORT FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adapt() {
        // TODO Remove ConfigModelCreator
        LoadTestConfiguration configModel = ConfigModelCreator.create();
        printLog(this.getClass(), "LOAD TEST CONFIG MODEL WAS CREATED");

        printLog(this.getClass(), "ADAPTING LOAD TEST");
        GatlingConfiguration configuration = adapter.adapt(configModel);
        printLog(this.getClass(), "LOAD TEST CONFIG WAS ADAPTED");

        ConfigStorage.setConfiguration(configuration);
        printLog(this.getClass(), "GATLING CONFIG WAS WRITTEN SUCCESSFULLY");
    }

    private void runGatling() {
        printLog(this.getClass(), "RUNNING GATLING");
        Path resultsPath = FileConfig.getResultFilePath();
        printLog(this.getClass(), "RESULTS WILL BE WRITTEN HERE: " + resultsPath);

        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder().simulationClass(DqSimulation.class.getName());
        Gatling.fromMap(props.build());
    }
}
