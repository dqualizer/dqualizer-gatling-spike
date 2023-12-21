package poc.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.dqlang.constants.GatlingConstants;
import poc.dqlang.constants.accuracy.Repetition;
import poc.dqlang.loadtest.LoadTestArtifact;
import poc.dqlang.loadtest.stimulus.*;
import poc.dqlang.loadtest.stimulus.loadprofile.ConstantLoad;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadIncrease;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadPeak;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadProfile;
import poc.exception.UnknownTypeException;
import poc.util.SymbolicTransformer;
import poc.util.ConstantsLoader;

@Component
public class StimulusAdapter {

    @Autowired
    private SymbolicTransformer transformer;
    @Autowired
    private ConstantsLoader constantsLoader;

    private final static String newLine = System.lineSeparator();

    public String adapt(LoadTestArtifact loadTest) {
        StringBuilder builder = new StringBuilder();
        builder.append("stimulus {" + newLine);

        Stimulus stimulus = loadTest.getStimulus();
        Workload workload = stimulus.getWorkload();
        builder.append(this.getWorkloadConfig(workload));

        builder.append("}" + newLine);
        return builder.toString();
    }

    private String getWorkloadConfig(Workload workload) {
        StringBuilder builder = new StringBuilder();
        String selectWorkload;
        String loadProfileConfig;

        if(workload.getType().equals(WorkloadType.OPEN)) {
            selectWorkload = "workload = open" + newLine;
            LoadProfile loadProfile = workload.getLoadProfile();
            loadProfileConfig = this.getOpenLoadProfileConfig(loadProfile);
        }
        else if(workload.getType().equals(WorkloadType.CLOSED)) {
            selectWorkload = "workload = closed" + newLine;
            LoadProfile loadProfile = workload.getLoadProfile();
            loadProfileConfig = this.getClosedLoadProfileConfig(loadProfile);
        }
        else throw new UnknownTypeException(workload.getClass().getName());

        builder.append(selectWorkload);
        builder.append(loadProfileConfig);
        return builder.toString();
    }

     private String getOpenLoadProfileConfig(LoadProfile loadProfile) {
         StringBuilder configBuilder = new StringBuilder();
         StringBuilder profileBuilder = new StringBuilder();
         String selectProfile;

         if(loadProfile instanceof LoadIncrease loadIncrease) {
             selectProfile = "profile = increase" + newLine;
             Number baseLoad  = transformer.calculateValue(loadIncrease.getBaseLoad());
             Number highestLoad = transformer.calculateValue(loadIncrease.getHighestLoad());
             Number timeToHighestLoad  = transformer.calculateValue(loadIncrease.getTimeToHighestLoad());
             Number constantDuration  = transformer.calculateValue(loadIncrease.getConstantDuration());

             Number adaptedBaseLoad = transformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD);
             Number adaptedHighestLoad = transformer.calculateTimeUnit(highestLoad, SymbolicTransformer.LOAD);
             Number adaptedTimeToHighestLoad = transformer.calculateTimeUnit(timeToHighestLoad, SymbolicTransformer.DURATION);
             Number adaptedConstantDuration = transformer.calculateTimeUnit(constantDuration, SymbolicTransformer.DURATION);

             profileBuilder.append("increase {" + newLine);
             profileBuilder.append("baseLoad = " + adaptedBaseLoad + newLine);
             profileBuilder.append("highestLoad = " + adaptedHighestLoad + newLine);
             profileBuilder.append("timeToHighestLoad = " + adaptedTimeToHighestLoad + newLine);
             profileBuilder.append("constantDuration = " + adaptedConstantDuration + newLine);
             profileBuilder.append("}" + newLine);
         }
         else if(loadProfile instanceof LoadPeak loadPeak) {
             selectProfile = "profile = peak" + newLine;
             Number baseLoad  = transformer.calculateValue(loadPeak.getBaseLoad());
             Number peakLoad = transformer.calculateValue(loadPeak.getPeakLoad());
             Number duration  = transformer.calculateValue(loadPeak.getDuration());

             Number adaptedBaseLoad = transformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD);
             Number adaptedPeakLoad = transformer.calculateTimeUnit(peakLoad, SymbolicTransformer.LOAD);
             Number adaptedDuration = transformer.calculateTimeUnit(duration, SymbolicTransformer.DURATION);

             profileBuilder.append("peak {" + newLine);
             profileBuilder.append("baseLoad = " + adaptedBaseLoad + newLine);
             profileBuilder.append("peakLoad = " + adaptedPeakLoad + newLine);
             profileBuilder.append("duration = " + adaptedDuration + newLine);
             profileBuilder.append("}" + newLine);
         }
         else if(loadProfile instanceof ConstantLoad constantLoad) {
             selectProfile = "profile = constant" + newLine;
             Number baseLoad  = transformer.calculateValue(constantLoad.getBaseLoad());
             Number targetLoad = transformer.calculateValue(constantLoad.getTargetLoad());
             Number duration  = transformer.calculateValue(constantLoad.getDuration());

             Number adaptedBaseLoad = transformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD);
             Number adaptedTargetLoad = transformer.calculateTimeUnit(targetLoad, SymbolicTransformer.LOAD);
             Number adaptedDuration = transformer.calculateTimeUnit(duration, SymbolicTransformer.DURATION);

             profileBuilder.append("constant {" + newLine);
             profileBuilder.append("baseLoad = " + adaptedBaseLoad + newLine);
             profileBuilder.append("peakLoad = " + adaptedTargetLoad + newLine);
             profileBuilder.append("duration = " + adaptedDuration + newLine);
             profileBuilder.append("}" + newLine);
         }
         else throw new UnknownTypeException(loadProfile.getClass().getName());

         configBuilder.append(selectProfile);
         configBuilder.append("injectOpen {" + newLine);
         configBuilder.append(profileBuilder);
         configBuilder.append("}" + newLine);

         return configBuilder.toString();
     }

    private String getClosedLoadProfileConfig(LoadProfile loadProfile) {
        StringBuilder configBuilder = new StringBuilder();
        StringBuilder profileBuilder = new StringBuilder();
        String selectProfile;

        if(loadProfile instanceof LoadIncrease loadIncrease) {
            selectProfile = "profile = increase" + newLine;
            Number baseLoad  = transformer.calculateValue(loadIncrease.getBaseLoad());
            Number highestLoad = transformer.calculateValue(loadIncrease.getHighestLoad());
            Number timeToHighestLoad  = transformer.calculateValue(loadIncrease.getTimeToHighestLoad());
            Number constantDuration  = transformer.calculateValue(loadIncrease.getConstantDuration());

            profileBuilder.append("increase {" + newLine);
            profileBuilder.append("baseLoad = " + baseLoad + newLine);
            profileBuilder.append("highestLoad = " + highestLoad + newLine);
            profileBuilder.append("timeToHighestLoad = " + timeToHighestLoad + newLine);
            profileBuilder.append("constantDuration = " + constantDuration + newLine);
            profileBuilder.append("}" + newLine);
        }
        else if(loadProfile instanceof LoadPeak loadPeak){
            selectProfile = "profile = peak" + newLine;
            Number baseLoad  = transformer.calculateValue(loadPeak.getBaseLoad());
            Number peakLoad = transformer.calculateValue(loadPeak.getPeakLoad());
            Number duration  = transformer.calculateValue(loadPeak.getDuration());

            profileBuilder.append("peak {" + newLine);
            profileBuilder.append("baseLoad = " + baseLoad + newLine);
            profileBuilder.append("peakLoad = " + peakLoad + newLine);
            profileBuilder.append("duration = " + duration + newLine);
            profileBuilder.append("}" + newLine);
        }
        else if(loadProfile instanceof ConstantLoad constantLoad) {
            selectProfile = "profile = constant" + newLine;
            Number baseLoad  = transformer.calculateValue(constantLoad.getBaseLoad());
            Number targetLoad = transformer.calculateValue(constantLoad.getTargetLoad());
            Number duration  = transformer.calculateValue(constantLoad.getDuration());

            profileBuilder.append("constant {" + newLine);
            profileBuilder.append("baseLoad = " + baseLoad + newLine);
            profileBuilder.append("targetLoad = " + targetLoad + newLine);
            profileBuilder.append("duration = " + duration + newLine);
            profileBuilder.append("}" + newLine);
        }
        else throw new UnknownTypeException(loadProfile.getClass().getName());

        configBuilder.append(selectProfile);
        configBuilder.append("injectClosed {" + newLine);
        configBuilder.append(profileBuilder);
        configBuilder.append("}" + newLine);

        return configBuilder.toString();
    }
}
