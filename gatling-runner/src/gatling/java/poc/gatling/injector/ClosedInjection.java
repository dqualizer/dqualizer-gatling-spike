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

public class ClosedInjection implements Injectable {
    private final Logger logger = Logger.getLogger(ClosedInjection.class.getName());
    private final int warmUpDuration;
    private final int coolDownDuration;

    public ClosedInjection() {
        GatlingConfiguration config = ConfigStorage.getConfiguration();
        TechnicalConstants technicalConfig = config.getTechnicalConstants();
        this.warmUpDuration = technicalConfig.getWarmUpDuration();
        this.coolDownDuration = technicalConfig.getCoolDownDuration();
    }

    @Override
    public PopulationBuilder createLoadIncreaseInjection(ScenarioBuilder scenarioBuilder, GatlingInjectionIncreaseProfile profile) {
        int baseLoad = (int) profile.getBaseLoad();
        int highestLoad = (int) profile.getHighestLoad();
        int timeToHighestLoad = profile.getTimeToHighestLoad();
        int constantDuration = profile.getConstantDuration();

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
    public PopulationBuilder createLoadPeakInjection(ScenarioBuilder scenarioBuilder, GatlingInjectionPeakProfile profile) {
        int baseLoad = (int) profile.getBaseLoad();
        int peakLoad = profile.getPeakLoad();
        int duration = profile.getDuration();

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
    public PopulationBuilder createConstantLoadInjection(ScenarioBuilder scenarioBuilder, GatlingInjectionConstantProfile profile) {
        int baseLoad = (int) profile.getBaseLoad();
        int targetLoad = (int) profile.getTargetLoad();
        int duration = profile.getDuration();

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
