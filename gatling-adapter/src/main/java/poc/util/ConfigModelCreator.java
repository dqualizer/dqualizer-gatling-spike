package poc.util;

import poc.dqlang.loadtest.*;
import poc.dqlang.loadtest.stimulus.*;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadIncrease;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadProfile;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicIntValue;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicValue;

import java.util.LinkedHashSet;

public class ConfigModelCreator {

    public static LoadTestConfiguration create() {
        LinkedHashSet<LoadTestArtifact> loadTests = new LinkedHashSet<>();
        LoadTestArtifact oneLoadTest = createLoadTestModel();
        loadTests.add(oneLoadTest);

        int version = 1;
        String context = "gatling-test";
        String environment = "dev";
        String baseURL = "https://reqres.in";

        LoadTestConfiguration config = new LoadTestConfiguration(version, context, environment, baseURL, loadTests);
        return config;
    }

    private static LoadTestArtifact createLoadTestModel() {
        Artifact artifact = new Artifact();
        String description = "Open dqualizer-user";
        ResponseMeasure responseMeasure = new ResponseMeasure();

        Stimulus stimulus = createStimulusModel();
        Endpoint endpoint = createEndpointModel();

        LoadTestArtifact loadTest = new LoadTestArtifact(artifact, description, stimulus, responseMeasure, endpoint);
        return loadTest;
    }

    private static Stimulus createStimulusModel() {
        SymbolicValue baseLoad = new SymbolicIntValue("MEDIUM");
        SymbolicValue highestLoad = new SymbolicIntValue("HIGH");
        SymbolicValue timeToHighestLoad = new SymbolicIntValue("SLOW");
        SymbolicValue constantDuration = new SymbolicIntValue("FAST");

        LoadProfile loadProfile = new LoadIncrease(baseLoad, highestLoad, timeToHighestLoad, constantDuration);
        Workload workload = new Workload(WorkloadType.OPEN, loadProfile);
        int accuracy = 100;

        Stimulus stimulus = new Stimulus(workload, accuracy);
        return  stimulus;
    }

    private static Endpoint createEndpointModel() {
        String field = "/api/users/{id}";
        String operation = "POST";
        String pathVariables = "loadtest/paths.json";
        String urlParameter = "loadtest/queryParams.json";
        String requestParameter = "loadtest/headers.json";
        String payload = "loadtest/payload.json";
        LinkedHashSet<Response> responses = new LinkedHashSet<>();
        Response response1 = new Response(200, null);
        Response response2 = new Response(201, null);
        responses.add(response1);
        responses.add(response2);

        Endpoint endpoint = new Endpoint(field, operation, pathVariables, urlParameter, requestParameter, payload, responses);
        return endpoint;
    }
}
