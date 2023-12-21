package poc.injector;

import com.typesafe.config.Config;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;

public interface Injectable {

    String newLine = System.lineSeparator();
    String INJECT_CLOSED = "injectClosed";
    String INJECT_OPEN = "injectOpen";

    PopulationBuilder createLoadIncreaseInjection(ScenarioBuilder scenarioBuilder, Config loadTest);
    PopulationBuilder createLoadPeakInjection(ScenarioBuilder scenarioBuilder, Config loadTest);
    PopulationBuilder createConstantLoadInjection(ScenarioBuilder scenarioBuilder, Config loadTest);
}
