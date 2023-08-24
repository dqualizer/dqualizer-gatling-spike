package poc.adapter;

import poc.dqlang.LoadTestConfig;

import java.util.List;

public class GatlingAdapter {

    private StimulusAdapter stimulusAdapter;
    private EndpointAdapter endpointAdapter;

    public GatlingAdapter() {
        this.stimulusAdapter = new StimulusAdapter();
        this.endpointAdapter = new EndpointAdapter();
    }

    public List<String> adapt(LoadTestConfig loadTestConfig) {
        return null;
    }
}
