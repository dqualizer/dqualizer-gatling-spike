package poc.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.dqlang.constants.LoadTestConstants;
import poc.dqlang.constants.accuracy.Repetition;
import poc.dqlang.gatling.GatlingConfiguration;
import poc.dqlang.gatling.GatlingLoadTest;
import poc.dqlang.gatling.action.GatlingAction;
import poc.dqlang.gatling.stimulus.GatlingStimulus;
import poc.dqlang.loadtest.LoadTestArtifact;
import poc.dqlang.loadtest.LoadTestConfiguration;
import poc.dqlang.loadtest.stimulus.Stimulus;
import poc.util.ConstantsLoader;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Component
public class GatlingAdapter {

    @Autowired
    private StimulusAdapter stimulusAdapter;
    @Autowired
    private EndpointAdapter endpointAdapter;

    private final LoadTestConstants constants = ConstantsLoader.load();

    public GatlingConfiguration adapt(LoadTestConfiguration loadTestConfig) {
        LinkedHashSet<LoadTestArtifact> loadTests = loadTestConfig.getLoadTests();
        List<GatlingLoadTest> gatlingLoadTests = new LinkedList<>();

        for (LoadTestArtifact loadTest : loadTests) {

            Stimulus stimulus = loadTest.getStimulus();
            int accuracy = stimulus.getAccuracy();
            int repetition = getRepetition(accuracy);

            GatlingStimulus gatlingStimulus = stimulusAdapter.adapt(stimulus);

            List<GatlingAction> actions = this.endpointAdapter.adapt(loadTest);

            GatlingLoadTest gatlingLoadTest = GatlingLoadTest.builder()
                    .repetition(repetition)
                    .stimulus(gatlingStimulus)
                    .actions(actions)
                    .build();

            gatlingLoadTests.add(gatlingLoadTest);
        }

        GatlingConfiguration configuration = GatlingConfiguration.builder()
                .context(loadTestConfig.getContext())
                .baseURL(loadTestConfig.getBaseURL())
                .technicalConstants(constants.getTechnicalConstants())
                .loadTests(gatlingLoadTests)
                .build();
        return configuration;
    }

    private int getRepetition(int accuracy) {
        Repetition repetitionConstants = constants.getAccuracy().getRepetition();

        int max = repetitionConstants.getMax();
        int min = repetitionConstants.getMin();

        int calculatedRepetition = (int)(max * (accuracy/100.0));
        int repetition = Math.max(calculatedRepetition, min);

        return repetition;
    }
}
