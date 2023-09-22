package poc.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.dqlang.loadtest.LoadTestArtifact;
import poc.dqlang.loadtest.LoadTestConfiguration;

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

    private final static String newLine = System.lineSeparator();

    public List<String> adapt(LoadTestConfiguration loadTestConfig) {
        List<String> configString = new LinkedList<>();
        configString.add(this.getConfigHeader());

        String baseURL = this.getBaseUrlConfig(loadTestConfig.getBaseURL());
        configString.add(baseURL);

        String technicalConfig = technicalAdapter.adapt();
        configString.add(technicalConfig);

        LinkedHashSet<LoadTestArtifact> loadTests = loadTestConfig.getLoadTests();
        if(loadTests.size() > 1)
            throw new UnsupportedOperationException("Currently only one loadtest can be executed");

        LoadTestArtifact loadTest = loadTests.iterator().next();
        String stimulusConfig = stimulusAdapter.adapt(loadTest);
        configString.add(stimulusConfig);
        String endpointConfig = endpointAdapter.adapt(loadTest);
        configString.add(endpointConfig);

        return configString;
    }

    private String getConfigHeader() {
        return """
                ###################################
                # dqualizer Gatling Configuration #
                ###################################
                
                """;
    }
    private String getBaseUrlConfig(String baseURL) {
        return "baseURL = \"" + baseURL + "\"" + newLine;
    }
}
