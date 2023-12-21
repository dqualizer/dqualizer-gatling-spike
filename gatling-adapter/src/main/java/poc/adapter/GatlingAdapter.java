package poc.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.dqlang.constants.GatlingConstants;
import poc.dqlang.constants.accuracy.Repetition;
import poc.dqlang.loadtest.LoadTestArtifact;
import poc.dqlang.loadtest.LoadTestConfiguration;
import poc.util.ConstantsLoader;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Component
public class GatlingAdapter {

    @Autowired
    private TechnicalAdapter technicalAdapter;
    @Autowired
    private StimulusAdapter stimulusAdapter;
    @Autowired
    private EndpointAdapter endpointAdapter;
    @Autowired
    private ConstantsLoader constantsLoader;

    private final static String newLine = System.lineSeparator();

    public List<String> adapt(LoadTestConfiguration loadTestConfig) {
        List<String> configString = new LinkedList<>();
        configString.add(this.getConfigHeader());

        String contextConfig = this.getContextConfig(loadTestConfig.getContext());
        configString.add(contextConfig);

        String baseUrlConfig = this.getBaseUrlConfig(loadTestConfig.getBaseURL());
        configString.add(baseUrlConfig);

        String technicalConfig = technicalAdapter.adapt();
        configString.add(technicalConfig);

        configString.add("loadTests = [" + newLine);
        LinkedHashSet<LoadTestArtifact> loadTests = loadTestConfig.getLoadTests();

        for (LoadTestArtifact loadTest : loadTests) {
            configString.add(" {" + newLine);

            String stimulusConfig = stimulusAdapter.adapt(loadTest);
            configString.add(stimulusConfig);

            int accuracy = loadTest.getStimulus().getAccuracy();
            String repetitionConfig = this.getRepetitionConfig(accuracy);
            configString.add(repetitionConfig + newLine);

            String endpointConfig = endpointAdapter.adapt(loadTest);
            configString.add(endpointConfig);

            configString.add(" }" + newLine);
        }
        configString.add("]");

        return configString;
    }

    private String getConfigHeader() {
        return """
                ###################################
                # dqualizer Gatling Configuration #
                ###################################
                
                """;
    }

    private String getContextConfig(String context) {
        return "context = \"" + context + "\"" + newLine;
    }

    private String getBaseUrlConfig(String baseURL) {
        return "baseURL = \"" + baseURL + "\"" + newLine;
    }

    private String getRepetitionConfig(int accuracy) {
        GatlingConstants constants = constantsLoader.load();

        Repetition repetitionConstants = constants.getAccuracy().getRepetition();

        int max = repetitionConstants.getMax();
        int min = repetitionConstants.getMin();

        int calculatedRepetition = (int)(max * (accuracy/100.0));
        int repetition = Math.max(calculatedRepetition, min);
        return "repetition = " + repetition + newLine;
    }
}
