package poc.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.dqlang.gatling.stimulus.*;
import poc.dqlang.loadtest.LoadTestArtifact;
import poc.dqlang.loadtest.stimulus.Stimulus;
import poc.dqlang.loadtest.stimulus.Workload;
import poc.dqlang.loadtest.stimulus.WorkloadType;
import poc.dqlang.loadtest.stimulus.loadprofile.ConstantLoad;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadIncrease;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadPeak;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadProfile;
import poc.exception.UnknownTypeException;
import poc.util.SymbolicTransformer;

@Component
public class StimulusAdapter {

    @Autowired
    private SymbolicTransformer transformer;

    public GatlingStimulus adapt(Stimulus stimulus) {

        Workload workload = stimulus.getWorkload();
        WorkloadType workloadType = workload.getType();
        LoadProfile loadProfile = workload.getLoadProfile();

        GatlingInjectionProfile injectionProfile = this.getInjectionProfile(loadProfile);
        GatlingInjection injection = new GatlingInjection(workloadType, injectionProfile);

        return new GatlingStimulus(injection);
    }

    private GatlingInjectionProfile getInjectionProfile(LoadProfile loadProfile) {
        if(loadProfile instanceof LoadIncrease loadIncrease) {
            Number baseLoad  = transformer.calculateValue(loadIncrease.getBaseLoad());
            Number highestLoad = transformer.calculateValue(loadIncrease.getHighestLoad());
            Number timeToHighestLoad  = transformer.calculateValue(loadIncrease.getTimeToHighestLoad());
            Number constantDuration  = transformer.calculateValue(loadIncrease.getConstantDuration());

            double adaptedBaseLoad = transformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD).doubleValue();
            double adaptedHighestLoad = transformer.calculateTimeUnit(highestLoad, SymbolicTransformer.LOAD).doubleValue();
            int adaptedTimeToHighestLoad = transformer.calculateTimeUnit(timeToHighestLoad, SymbolicTransformer.DURATION).intValue();
            int adaptedConstantDuration = transformer.calculateTimeUnit(constantDuration, SymbolicTransformer.DURATION).intValue();

            return new GatlingInjectionIncreaseProfile(adaptedBaseLoad, adaptedHighestLoad, adaptedTimeToHighestLoad, adaptedConstantDuration);
        }
        else if(loadProfile instanceof LoadPeak loadPeak) {
            Number baseLoad  = transformer.calculateValue(loadPeak.getBaseLoad());
            Number peakLoad = transformer.calculateValue(loadPeak.getPeakLoad());
            Number duration  = transformer.calculateValue(loadPeak.getDuration());

            double adaptedBaseLoad = transformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD).doubleValue();
            int adaptedPeakLoad = transformer.calculateTimeUnit(peakLoad, SymbolicTransformer.LOAD).intValue();
            int adaptedDuration = transformer.calculateTimeUnit(duration, SymbolicTransformer.DURATION).intValue();

            return new GatlingInjectionPeakProfile(adaptedBaseLoad, adaptedPeakLoad, adaptedDuration);
        }
        else if(loadProfile instanceof ConstantLoad constantLoad) {
            Number baseLoad  = transformer.calculateValue(constantLoad.getBaseLoad());
            Number targetLoad = transformer.calculateValue(constantLoad.getTargetLoad());
            Number duration  = transformer.calculateValue(constantLoad.getDuration());

            double adaptedBaseLoad = transformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD).doubleValue();
            double adaptedTargetLoad = transformer.calculateTimeUnit(targetLoad, SymbolicTransformer.LOAD).doubleValue();
            int adaptedDuration = transformer.calculateTimeUnit(duration, SymbolicTransformer.DURATION).intValue();

            return new GatlingInjectionConstantProfile(adaptedBaseLoad, adaptedTargetLoad, adaptedDuration);
        }
        else throw new UnknownTypeException(loadProfile.getClass().getName());
    }
}
