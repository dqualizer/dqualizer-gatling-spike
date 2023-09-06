package poc.adapter;

import org.apache.commons.lang3.NotImplementedException;
import poc.dqlang.loadtest.LoadTest;
import poc.dqlang.loadtest.LoadTestConfig;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class GatlingAdapter {

    private final StimulusAdapter stimulusAdapter;
    private final EndpointAdapter endpointAdapter;
    private final TechnicalAdapter technicalAdapter;
    private final static String newLine = System.lineSeparator();


    public GatlingAdapter() {
        this.stimulusAdapter = new StimulusAdapter();
        this.endpointAdapter = new EndpointAdapter();
        this.technicalAdapter = new TechnicalAdapter();
    }

    public List<String> adapt(LoadTestConfig loadTestConfig) {
        List<String> configString = new LinkedList<>();
        configString.add(this.getConfigHeader());

        String baseURL = this.getBaseUrlConfig(loadTestConfig.getBaseURL());
        configString.add(baseURL);

        String technicalConfig = technicalAdapter.adapt();
        configString.add(technicalConfig);

        LinkedHashSet<LoadTest> loadTests = loadTestConfig.getLoadTests();
        if(loadTests.size() > 1)
            throw new NotImplementedException("Currently only one loadtest can be executed");

        LoadTest loadTest = loadTests.iterator().next();
        String stimulusConfig = stimulusAdapter.adapt(loadTest);
        configString.add(stimulusConfig);
        //String endpointConfig = endpointAdapter.adapt(loadTest);
        //configString.add(endpointConfig);

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
