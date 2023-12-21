package poc.injector;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import poc.config.FileConfig;

import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ClosedInjection implements Injectable {
    private final Logger logger = Logger.getLogger(ClosedInjection.class.getName());
    private final int warmUpDuration;
    private final int coolDownDuration;
    private final Config injection;

    public ClosedInjection(Config stimulus) {
        String configPath = FileConfig.getLocalGatlingConfigPath();
        Config technicalConfig = ConfigFactory.load(configPath).getConfig("technicalConstants");
        this.warmUpDuration = technicalConfig.getInt("warmUpDuration");
        this.coolDownDuration = technicalConfig.getInt("coolDownDuration");
        this.injection = stimulus.getConfig(INJECT_CLOSED);
    }

    @Override
    public PopulationBuilder createLoadIncreaseInjection(ScenarioBuilder scenarioBuilder, Config loadTest) {
        Config increaseConfig = injection.getConfig("increase");
        int baseLoad = increaseConfig.getInt("baseLoad");
        int highestLoad = increaseConfig.getInt("highestLoad");
        int timeToHighestLoad = increaseConfig.getInt("timeToHighestLoad");
        int constantDuration = increaseConfig.getInt("constantDuration");

        logger.info("Running CLOSED LOAD INCREASE with" + newLine +
                "\t BASE LOAD : " + baseLoad + " users" + newLine +
                "\t HIGHEST LOAD: " + highestLoad + " users" + newLine +
                "\t TIME TO HIGHEST LOAD: " + timeToHighestLoad + " seconds" + newLine +
                "\t CONSTANT DURATION: " + constantDuration + " seconds"
        );

        return scenarioBuilder.injectClosed(
                rampConcurrentUsers(0).to(baseLoad).during(warmUpDuration),
                rampConcurrentUsers(baseLoad).to(highestLoad).during(timeToHighestLoad),
                constantConcurrentUsers(highestLoad).during(constantDuration),
                rampConcurrentUsers(highestLoad).to(0).during(coolDownDuration)
        );
    }

    @Override
    public PopulationBuilder createLoadPeakInjection(ScenarioBuilder scenarioBuilder, Config loadTest) {
        Config increaseConfig = injection.getConfig("peak");
        int baseLoad = increaseConfig.getInt("baseLoad");
        int peakLoad = increaseConfig.getInt("peakLoad");
        int duration = increaseConfig.getInt("duration");

        logger.info("Running CLOSED LOAD PEAK with" + newLine +
                "\t BASE LOAD : " + baseLoad + " users" + newLine +
                "\t PEAK LOAD: " + peakLoad + " users" + newLine +
                "\t DURATION: " + duration + " seconds"
        );

        return scenarioBuilder.injectClosed(
                rampConcurrentUsers(0).to(baseLoad).during(warmUpDuration),
                rampConcurrentUsers(baseLoad).to(peakLoad).during(duration),
                rampConcurrentUsers(peakLoad).to(0).during(coolDownDuration)
        );
    }

    @Override
    public PopulationBuilder createConstantLoadInjection(ScenarioBuilder scenarioBuilder, Config loadTest) {
        Config increaseConfig = injection.getConfig("constant");
        int baseLoad = increaseConfig.getInt("baseLoad");
        int targetLoad = increaseConfig.getInt("targetLoad");
        int duration = increaseConfig.getInt("duration");

        logger.info("Running CLOSED CONSTANT LOAD with" + newLine +
                "\t BASE LOAD : " + baseLoad + " users" + newLine +
                "\t TARGET LOAD: " + targetLoad + " users" + newLine +
                "\t DURATION: " + duration + " seconds"
        );

        return scenarioBuilder.injectClosed(
                constantConcurrentUsers(baseLoad).during(warmUpDuration),
                constantConcurrentUsers(targetLoad).during(duration),
                constantConcurrentUsers(baseLoad).during(coolDownDuration)
        );
    }
}
