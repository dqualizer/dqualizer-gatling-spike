package poc.gatling.injector;

import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import poc.dqlang.gatling.stimulus.GatlingInjectionConstantProfile;
import poc.dqlang.gatling.stimulus.GatlingInjectionIncreaseProfile;
import poc.dqlang.gatling.stimulus.GatlingInjectionPeakProfile;

public interface Injectable {

    String newLine = System.lineSeparator();

    PopulationBuilder createLoadIncreaseInjection(ScenarioBuilder scenarioBuilder, GatlingInjectionIncreaseProfile profile);
    PopulationBuilder createLoadPeakInjection(ScenarioBuilder scenarioBuilder, GatlingInjectionPeakProfile profile);
    PopulationBuilder createConstantLoadInjection(ScenarioBuilder scenarioBuilder, GatlingInjectionConstantProfile profile);
}
