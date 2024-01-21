package poc.gatling.helper;

import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import poc.dqlang.gatling.GatlingLoadTest;
import poc.dqlang.gatling.stimulus.*;
import poc.dqlang.loadtest.stimulus.WorkloadType;
import poc.exception.UnknownTypeException;
import poc.gatling.injector.ClosedInjection;
import poc.gatling.injector.Injectable;
import poc.gatling.injector.OpenInjection;

public class InjectionHelper {

    public PopulationBuilder getPopulationBuilder(ScenarioBuilder scenario, GatlingLoadTest loadTest) {
        GatlingStimulus stimulus = loadTest.getStimulus();
        GatlingInjection injection = stimulus.getInjection();
        WorkloadType workload = injection.getWorkloadType();
        Injectable injectable = this.getInjection(workload);
        GatlingInjectionProfile profile = injection.getProfile();

        if(profile instanceof GatlingInjectionIncreaseProfile increaseProfile) {
            return injectable.createLoadIncreaseInjection(scenario, increaseProfile);
        }
        else if(profile instanceof GatlingInjectionPeakProfile peakProfile) {
            return injectable.createLoadPeakInjection(scenario, peakProfile);
        }
        else if(profile instanceof GatlingInjectionConstantProfile constantProfile) {
            return injectable.createConstantLoadInjection(scenario, constantProfile);
        }
        else {
            throw new UnknownTypeException(profile.toString());
        }
    }

    private Injectable getInjection(WorkloadType workload) {
        if(workload.equals(WorkloadType.OPEN)) return new OpenInjection();
        else if(workload.equals(WorkloadType.CLOSED)) return new ClosedInjection();
        else throw new UnknownTypeException(workload.toString());
    }
}
