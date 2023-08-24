package poc.injector;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import poc.config.FileConfig;

import static io.gatling.javaapi.core.CoreDsl.*;

public class OpenInjection implements Injectable {

    private final String configPath = FileConfig.getGatlingConfigPath();
    private final Config injection = ConfigFactory.load(configPath).getConfig("stimulus.injectOpen");

    @Override
    public PopulationBuilder createLoadIncreaseInjection(ScenarioBuilder scenarioBuilder) {
        Config increaseConfig = injection.getConfig("increase");
        double baseLoad = increaseConfig.getDouble("baseLoad");
        double highestLoad = increaseConfig.getDouble("highestLoad");
        int timeToHighestLoad = increaseConfig.getInt("timeToHighestLoad");
        int constantDuration = increaseConfig.getInt("constantDuration");

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

        return scenarioBuilder.injectOpen(
                rampUsersPerSec(0).to(baseLoad).during(warmUpDuration),
                constantUsersPerSec(targetLoad).during(duration),
                rampUsersPerSec(baseLoad).to(0).during(coolDownDuration)
        );
    }
}
