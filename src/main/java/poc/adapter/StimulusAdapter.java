package poc.adapter;

import poc.dqlang.constants.GatlingConstants;
import poc.dqlang.constants.accuracy.Repetition;
import poc.dqlang.loadtest.LoadTest;
import poc.dqlang.loadtest.stimulus.*;
import poc.exception.UnknownTypeException;
import poc.util.SymbolicTransformer;
import poc.util.ConstantsLoader;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StimulusAdapter {

    private final static String newLine = System.lineSeparator();

    public String adapt(LoadTest loadTest) {
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

        if(workload instanceof OpenWorkload openWorkload) {
            selectWorkload = "workload = open" + newLine;
            LoadProfile loadProfile = openWorkload.getLoadProfile();
            loadProfileConfig = this.getOpenLoadProfileConfig(loadProfile);
        }
        else if(workload instanceof ClosedWorkload closedWorkload) {
            selectWorkload = "workload = closed" + newLine;
            LoadProfile loadProfile = closedWorkload.getLoadProfile();
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
             Number adaptedBaseLoad = SymbolicTransformer.calculateTimeUnit(baseLoad, SymbolicTransformer.LOAD);
             Number highestLoad = SymbolicTransformer.calculateValue(loadIncrease.getHighestLoad());
             Number adaptedHighestLoad = SymbolicTransformer.calculateTimeUnit(highestLoad, SymbolicTransformer.LOAD);
             Number timeToHighestLoad  = SymbolicTransformer.calculateValue(loadIncrease.getTimeToHighestLoad());
             Number adaptedTimeToHighestLoad = SymbolicTransformer.calculateTimeUnit(timeToHighestLoad, SymbolicTransformer.DURATION);
             Number constantDuration  = SymbolicTransformer.calculateValue(loadIncrease.getConstantDuration());
             Number adaptedConstantDuration = SymbolicTransformer.calculateTimeUnit(constantDuration, SymbolicTransformer.DURATION);

             profileBuilder.append("baseLoad = " + adaptedBaseLoad + newLine);
             profileBuilder.append("highestLoad = " + adaptedHighestLoad + newLine);
             profileBuilder.append("timeToHighestLoad = " + adaptedTimeToHighestLoad + newLine);
             profileBuilder.append("constantDuration = " + adaptedConstantDuration + newLine);
         }
         else if(loadProfile instanceof LoadPeak loadPeak) {
             selectProfile = "profile = peak" + newLine;
         }
         else if(loadProfile instanceof ConstantLoad constantLoad) {
             selectProfile = "profile = constant" + newLine;
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

            profileBuilder.append("baseLoad = " + baseLoad + newLine);
            profileBuilder.append("highestLoad = " + highestLoad + newLine);
            profileBuilder.append("timeToHighestLoad = " + timeToHighestLoad + newLine);
            profileBuilder.append("constantDuration = " + constantDuration + newLine);
        }
        else if(loadProfile instanceof LoadPeak loadPeak){
            selectProfile = "profile = peak" + newLine;
        }
        else if(loadProfile instanceof ConstantLoad constantLoad) {
            selectProfile = "profile = constant" + newLine;
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
