package poc.injector;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import poc.config.FileConfig;

import static io.gatling.javaapi.core.CoreDsl.*;

//TODO Lastkurven sehen noch ganz fragwürdig aus
public class ClosedInjection implements Injectable {

    private final String configPath = FileConfig.getGatlingConfigPath();
    private final Config injection = ConfigFactory.load(configPath).getConfig("stimulus.injectClosed");

    private final int warmUpDuration;
    private final int coolDownDuration;

    public ClosedInjection() {
        Config technicalConfig = ConfigFactory.load(configPath).getConfig("technical");
        this.warmUpDuration = technicalConfig.getInt("warmUpDuration");
        this.coolDownDuration = technicalConfig.getInt("coolDownDuration");
    }

    @Override
    public PopulationBuilder createLoadIncreaseInjection(ScenarioBuilder scenarioBuilder) {
        Config increaseConfig = injection.getConfig("increase");
        int baseLoad = increaseConfig.getInt("baseLoad");
        int highestLoad = increaseConfig.getInt("highestLoad");
        int timeToHighestLoad = increaseConfig.getInt("timeToHighestLoad");
        int constantDuration = increaseConfig.getInt("constantDuration");

        return scenarioBuilder.injectClosed(
                rampConcurrentUsers(0).to(baseLoad).during(warmUpDuration),
                rampConcurrentUsers(baseLoad).to(highestLoad).during(timeToHighestLoad),
                constantConcurrentUsers(highestLoad).during(constantDuration),
                rampConcurrentUsers(highestLoad).to(0).during(coolDownDuration)
        );
    }

    @Override
    public PopulationBuilder createLoadPeakInjection(ScenarioBuilder scenarioBuilder) {
        Config increaseConfig = injection.getConfig("peak");
        int baseLoad = increaseConfig.getInt("baseLoad");
        int peakLoad = increaseConfig.getInt("peakLoad");
        int duration = increaseConfig.getInt("duration");


        return scenarioBuilder.injectClosed(
                rampConcurrentUsers(0).to(baseLoad).during(warmUpDuration),
                rampConcurrentUsers(baseLoad).to(peakLoad).during(duration),
                rampConcurrentUsers(peakLoad).to(0).during(coolDownDuration)
        );
    }

    @Override
    public PopulationBuilder createConstantLoadInjection(ScenarioBuilder scenarioBuilder) {
        Config increaseConfig = injection.getConfig("constant");
        int baseLoad = increaseConfig.getInt("baseLoad");
        int targetLoad = increaseConfig.getInt("targetLoad");
        int duration = increaseConfig.getInt("duration");

        return scenarioBuilder.injectClosed(
                constantConcurrentUsers(baseLoad).during(warmUpDuration),
                constantConcurrentUsers(targetLoad).during(duration),
                constantConcurrentUsers(baseLoad).during(coolDownDuration)
        );
    }
}
