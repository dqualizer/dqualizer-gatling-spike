package poc.adapter;

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

    private final static String newLine = System.lineSeparator();

    public String adapt(LoadTestArtifact loadTest) {
        StringBuilder builder = new StringBuilder();
        builder.append("stimulus {" + newLine);

        Stimulus stimulus = loadTest.getStimulus();
        int accuracy = stimulus.getAccuracy();
        builder.append(this.getRepetitionConfig(accuracy));

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
             Number baseLoad  = SymbolicTransformer.calculateValue(loadIncrease.getBaseLoad());
             Number highestLoad = SymbolicTransformer.calculateValue(loadIncrease.getHighestLoad());
             Number timeToHighestLoad  = SymbolicTransformer.calculateValue(loadIncrease.getTimeToHighestLoad());
             Number constantDuration  = SymbolicTransformer.calculateValue(loadIncrease.getConstantDuration());

             Number adaptedBaseLoad = SymbolicTransformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD);
             Number adaptedHighestLoad = SymbolicTransformer.calculateTimeUnit(highestLoad, SymbolicTransformer.LOAD);
             Number adaptedTimeToHighestLoad = SymbolicTransformer.calculateTimeUnit(timeToHighestLoad, SymbolicTransformer.DURATION);
             Number adaptedConstantDuration = SymbolicTransformer.calculateTimeUnit(constantDuration, SymbolicTransformer.DURATION);

             profileBuilder.append("increase {" + newLine);
             profileBuilder.append("baseLoad = " + adaptedBaseLoad + newLine);
             profileBuilder.append("highestLoad = " + adaptedHighestLoad + newLine);
             profileBuilder.append("timeToHighestLoad = " + adaptedTimeToHighestLoad + newLine);
             profileBuilder.append("constantDuration = " + adaptedConstantDuration + newLine);
             profileBuilder.append("}" + newLine);
         }
         else if(loadProfile instanceof LoadPeak loadPeak) {
             selectProfile = "profile = peak" + newLine;
             Number baseLoad  = SymbolicTransformer.calculateValue(loadPeak.getBaseLoad());
             Number peakLoad = SymbolicTransformer.calculateValue(loadPeak.getPeakLoad());
             Number duration  = SymbolicTransformer.calculateValue(loadPeak.getDuration());

             Number adaptedBaseLoad = SymbolicTransformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD);
             Number adaptedPeakLoad = SymbolicTransformer.calculateTimeUnit(peakLoad, SymbolicTransformer.LOAD);
             Number adaptedDuration = SymbolicTransformer.calculateTimeUnit(duration, SymbolicTransformer.DURATION);

             profileBuilder.append("peak {" + newLine);
             profileBuilder.append("baseLoad = " + adaptedBaseLoad + newLine);
             profileBuilder.append("peakLoad = " + adaptedPeakLoad + newLine);
             profileBuilder.append("duration = " + adaptedDuration + newLine);
             profileBuilder.append("}" + newLine);
         }
         else if(loadProfile instanceof ConstantLoad constantLoad) {
             selectProfile = "profile = constant" + newLine;
             Number baseLoad  = SymbolicTransformer.calculateValue(constantLoad.getBaseLoad());
             Number targetLoad = SymbolicTransformer.calculateValue(constantLoad.getTargetLoad());
             Number duration  = SymbolicTransformer.calculateValue(constantLoad.getDuration());

             Number adaptedBaseLoad = SymbolicTransformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD);
             Number adaptedTargetLoad = SymbolicTransformer.calculateTimeUnit(targetLoad, SymbolicTransformer.LOAD);
             Number adaptedDuration = SymbolicTransformer.calculateTimeUnit(duration, SymbolicTransformer.DURATION);

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
            Number baseLoad  = SymbolicTransformer.calculateValue(loadIncrease.getBaseLoad());
            Number highestLoad = SymbolicTransformer.calculateValue(loadIncrease.getHighestLoad());
            Number timeToHighestLoad  = SymbolicTransformer.calculateValue(loadIncrease.getTimeToHighestLoad());
            Number constantDuration  = SymbolicTransformer.calculateValue(loadIncrease.getConstantDuration());

            profileBuilder.append("increase {" + newLine);
            profileBuilder.append("baseLoad = " + baseLoad + newLine);
            profileBuilder.append("highestLoad = " + highestLoad + newLine);
            profileBuilder.append("timeToHighestLoad = " + timeToHighestLoad + newLine);
            profileBuilder.append("constantDuration = " + constantDuration + newLine);
            profileBuilder.append("}" + newLine);
        }
        else if(loadProfile instanceof LoadPeak loadPeak){
            selectProfile = "profile = peak" + newLine;
            Number baseLoad  = SymbolicTransformer.calculateValue(loadPeak.getBaseLoad());
            Number peakLoad = SymbolicTransformer.calculateValue(loadPeak.getPeakLoad());
            Number duration  = SymbolicTransformer.calculateValue(loadPeak.getDuration());

            profileBuilder.append("peak {" + newLine);
            profileBuilder.append("baseLoad = " + baseLoad + newLine);
            profileBuilder.append("peakLoad = " + peakLoad + newLine);
            profileBuilder.append("duration = " + duration + newLine);
            profileBuilder.append("}" + newLine);
        }
        else if(loadProfile instanceof ConstantLoad constantLoad) {
            selectProfile = "profile = constant" + newLine;
            Number baseLoad  = SymbolicTransformer.calculateValue(constantLoad.getBaseLoad());
            Number targetLoad = SymbolicTransformer.calculateValue(constantLoad.getTargetLoad());
            Number duration  = SymbolicTransformer.calculateValue(constantLoad.getDuration());

            profileBuilder.append("constant {" + newLine);
            profileBuilder.append("baseLoad = " + baseLoad + newLine);
            profileBuilder.append("targetLoad = " + targetLoad + newLine);
            profileBuilder.append("duration = " + duration + newLine);
            profileBuilder.append("}" + newLine);
        }
        else throw new UnknownTypeException(loadProfile.getClass().getName());
        profileBuilder.append("}" + newLine);

        configBuilder.append(selectProfile);
        configBuilder.append("injectClosed {" + newLine);
        configBuilder.append(profileBuilder);
        configBuilder.append("}" + newLine);

        return configBuilder.toString();
    }

    private String getRepetitionConfig(int accuracy) {
        GatlingConstants constants = ConstantsLoader.load();

        Repetition repetitionConstants = constants.getAccuracy().getRepetition();

        int max = repetitionConstants.getMax();
        int min = repetitionConstants.getMin();

        int calculatedRepetition = (int)(max * (accuracy/100.0));
        int repetition = Math.max(calculatedRepetition, min);
        return "repetition = " + repetition + newLine;
    }
}
