package poc.injector;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import poc.config.FileConfig;
import poc.simulation.DqSimulation;

import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.*;

public class OpenInjection implements Injectable {
    private final Logger logger = Logger.getLogger(OpenInjection.class.getName());
    private final String newLine = System.lineSeparator();

    private final String configPath = FileConfig.getLocalGatlingConfigPath();
    private final Config stimulus = ConfigFactory.load(configPath).getConfig("stimulus");
    private final Config injection = stimulus.getConfig("injectOpen");

    private final int warmUpDuration;
    private final int coolDownDuration;

    public OpenInjection() {
        Config technicalConfig = ConfigFactory.load(configPath).getConfig("technicalConstants");
        this.warmUpDuration = technicalConfig.getInt("warmUpDuration");
        this.coolDownDuration = technicalConfig.getInt("coolDownDuration");
    }

    @Override
    public PopulationBuilder createLoadIncreaseInjection(ScenarioBuilder scenarioBuilder) {
        Config increaseConfig = injection.getConfig("increase");
        double baseLoad = increaseConfig.getDouble("baseLoad");
        double highestLoad = increaseConfig.getDouble("highestLoad");
        int timeToHighestLoad = increaseConfig.getInt("timeToHighestLoad");
        int constantDuration = increaseConfig.getInt("constantDuration");

        logger.info("Running OPEN LOAD INCREASE with" + newLine +
                        "\t BASE LOAD: " + baseLoad + " users" + newLine +
                        "\t HIGHEST LOAD: " + highestLoad + " users" + newLine +
                        "\t TIME TO HIGHEST LOAD: " + timeToHighestLoad + " seconds" + newLine +
                        "\t CONSTANT DURATION: " + constantDuration + " seconds"
        );

        return scenarioBuilder.injectOpen(
                    rampUsersPerSec(0).to(baseLoad).during(warmUpDuration),
                    rampUsersPerSec(baseLoad).to(highestLoad).during(timeToHighestLoad),
                    constantUsersPerSec(highestLoad).during(constantDuration),
                    rampUsersPerSec(highestLoad).to(0).during(coolDownDuration)
                );
    }

    @Override
    public PopulationBuilder createLoadPeakInjection(ScenarioBuilder scenarioBuilder) {
        Config increaseConfig = injection.getConfig("peak");
        int baseLoad = increaseConfig.getInt("baseLoad");
        int peakLoad = increaseConfig.getInt("peakLoad");
        int duration = increaseConfig.getInt("duration");

        logger.info("Running OPEN LOAD PEAK with" + newLine +
                "\t BASE LOAD: " + baseLoad + " users" + newLine +
                "\t PEAK LOAD: " + peakLoad + " users" + newLine +
                "\t DURATION: " + duration + " seconds"
        );

        //TODO Lastkurve sieht noch fragwürdig aus
        return scenarioBuilder.injectOpen(
                rampUsersPerSec(0).to(baseLoad).during(warmUpDuration),
                stressPeakUsers(peakLoad).during(duration),
                rampUsersPerSec(peakLoad).to(0).during(coolDownDuration)
        );
    }

    @Override
    public PopulationBuilder createConstantLoadInjection(ScenarioBuilder scenarioBuilder) {
        Config increaseConfig = injection.getConfig("constant");
        int baseLoad = increaseConfig.getInt("baseLoad");
        double targetLoad = increaseConfig.getDouble("targetLoad");
        int duration = increaseConfig.getInt("duration");

        logger.info("Running OPEN CONSTANT LOAD with" + newLine +
                "\t BASE LOAD: " + baseLoad + " users" + newLine +
                "\t TARGET LOAD: " + targetLoad + " users" + newLine +
                "\t DURATION: " + duration + " seconds"
        );

        return scenarioBuilder.injectOpen(
                rampUsersPerSec(0).to(baseLoad).during(warmUpDuration),
                constantUsersPerSec(targetLoad).during(duration),
                rampUsersPerSec(baseLoad).to(0).during(coolDownDuration)
        );
    }
}
