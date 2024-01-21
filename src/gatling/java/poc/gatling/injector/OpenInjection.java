package poc.gatling.injector;

import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import poc.dqlang.constants.TechnicalConstants;
import poc.dqlang.gatling.GatlingConfiguration;
import poc.dqlang.gatling.stimulus.GatlingInjection;
import poc.dqlang.gatling.stimulus.GatlingInjectionConstantProfile;
import poc.dqlang.gatling.stimulus.GatlingInjectionIncreaseProfile;
import poc.dqlang.gatling.stimulus.GatlingInjectionPeakProfile;
import poc.service.ConfigStorage;

import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.*;
import static poc.util.CustomLogger.printLog;

public class OpenInjection implements Injectable {
    private final int warmUpDuration;
    private final int coolDownDuration;

    public OpenInjection() {
        GatlingConfiguration config = ConfigStorage.getConfiguration();
        TechnicalConstants technicalConfig = config.getTechnicalConstants();
        this.warmUpDuration = technicalConfig.getWarmUpDuration();
        this.coolDownDuration = technicalConfig.getCoolDownDuration();
    }

    @Override
    public PopulationBuilder createLoadIncreaseInjection(ScenarioBuilder scenarioBuilder, GatlingInjectionIncreaseProfile profile) {
        double baseLoad = profile.getBaseLoad();
        double highestLoad = profile.getHighestLoad();
        int timeToHighestLoad = profile.getTimeToHighestLoad();
        int constantDuration = profile.getConstantDuration();

        printLog(this.getClass(),
                "Running CLOSED LOAD INCREASE with" + newLine +
                        "\t BASE LOAD : " + baseLoad + " users" + newLine +
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
    public PopulationBuilder createLoadPeakInjection(ScenarioBuilder scenarioBuilder, GatlingInjectionPeakProfile profile) {
        double baseLoad = profile.getBaseLoad();
        int peakLoad = profile.getPeakLoad();
        int duration = profile.getDuration();

        printLog(this.getClass(),
                "Running CLOSED LOAD PEAK with" + newLine +
                        "\t BASE LOAD : " + baseLoad + " users" + newLine +
                        "\t PEAK LOAD: " + peakLoad + " users" + newLine +
                        "\t DURATION: " + duration + " seconds"
        );

        // since stressPeakUsers() will drop the users per second anyway, there is no need for a warm-up
        // maybe we can replace it with rampUsersPerSec()
        return scenarioBuilder.injectOpen(
                //rampUsersPerSec(0).to(baseLoad).during(warmUpDuration),
                stressPeakUsers(peakLoad).during(duration),
                rampUsersPerSec(peakLoad).to(0).during(coolDownDuration)
        );
    }

    @Override
    public PopulationBuilder createConstantLoadInjection(ScenarioBuilder scenarioBuilder, GatlingInjectionConstantProfile profile) {
        double baseLoad = profile.getBaseLoad();
        double targetLoad = profile.getTargetLoad();
        int duration = profile.getDuration();

        printLog(this.getClass(),
                "Running CLOSED CONSTANT LOAD with" + newLine +
                        "\t BASE LOAD : " + baseLoad + " users" + newLine +
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
