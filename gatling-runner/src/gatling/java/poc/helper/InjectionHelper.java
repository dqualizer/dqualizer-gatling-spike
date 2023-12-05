package poc.helper;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import net.sf.saxon.om.Chain;
import poc.config.FileConfig;
import poc.exception.UnknownTypeException;
import poc.injector.ClosedInjection;
import poc.injector.Injectable;
import poc.injector.OpenInjection;

import java.util.logging.Logger;

public class InjectionHelper {
    private final Config stimulus;

    public InjectionHelper(Config config) {
        this.stimulus = config.getConfig("stimulus");
    }

    public PopulationBuilder getPopulationBuilder(ScenarioBuilder scenario) {
        String workload = stimulus.getString("workload");
        String profile = stimulus.getString("profile");
        Injectable injectable = this.getInjection(workload);

        switch (profile) {
            case "increase" -> {
                return injectable.createLoadIncreaseInjection(scenario);
            }
            case "peak" -> {
                return injectable.createLoadPeakInjection(scenario);
            }
            case "constant" -> {
                return injectable.createConstantLoadInjection(scenario);
            }
            default -> throw new UnknownTypeException(profile);
        }
    }

    private Injectable getInjection(String workload) {
        if(workload.equals("open")) return new OpenInjection();
        else if(workload.equals("closed")) return new ClosedInjection();
        else throw new UnknownTypeException(workload);
    }
}
