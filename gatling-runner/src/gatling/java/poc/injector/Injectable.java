package poc.injector;

import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;

public interface Injectable {

    PopulationBuilder createLoadIncreaseInjection(ScenarioBuilder scenarioBuilder);
    PopulationBuilder createLoadPeakInjection(ScenarioBuilder scenarioBuilder);
    PopulationBuilder createConstantLoadInjection(ScenarioBuilder scenarioBuilder);
}
