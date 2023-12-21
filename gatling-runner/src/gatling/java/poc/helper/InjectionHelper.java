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

    public PopulationBuilder getPopulationBuilder(ScenarioBuilder scenario, Config loadTest) {
        Config stimulus = loadTest.getConfig("stimulus");
        String workload = stimulus.getString("workload");
        Injectable injectable = this.getInjection(workload, stimulus);
        String profile = stimulus.getString("profile");

        switch (profile) {
            case "increase" -> {
                return injectable.createLoadIncreaseInjection(scenario, loadTest);
            }
            case "peak" -> {
                return injectable.createLoadPeakInjection(scenario, loadTest);
            }
            case "constant" -> {
                return injectable.createConstantLoadInjection(scenario, loadTest);
            }
            default -> throw new UnknownTypeException(profile);
        }
    }

    private Injectable getInjection(String workload, Config stimulus) {
        if(workload.equals("open")) return new OpenInjection(stimulus);
        else if(workload.equals("closed")) return new ClosedInjection(stimulus);
        else throw new UnknownTypeException(workload);
    }
}
