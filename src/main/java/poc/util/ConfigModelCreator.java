package poc.util;

import poc.dqlang.loadtest.*;
import poc.dqlang.loadtest.stimulus.*;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicIntValue;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicValue;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConfigModelCreator {

    public static LoadTestConfig create() {
        LinkedHashSet<LoadTest> loadTests = new LinkedHashSet<>();
        LoadTest oneLoadTest = createLoadTestModel();
        loadTests.add(oneLoadTest);

        int version = 1;
        String context = "gatling-test";
        String environment = "dev";
        String baseURL = "https://reqres.in";

        LoadTestConfig config = new LoadTestConfig(version, context, environment, baseURL, loadTests);
        return config;
    }

    private static LoadTest createLoadTestModel() {
        Artifact artifact = new Artifact();
        String description = "Open dqualizer-user";
        ResponseMeasure responseMeasure = new ResponseMeasure();

        Stimulus stimulus = createStimulusModel();
        Endpoint endpoint = createEndpointModel();

        LoadTest loadTest = new LoadTest(artifact, description, stimulus, responseMeasure, endpoint);
        return loadTest;
    }

    private static Stimulus createStimulusModel() {
        SymbolicValue baseLoad = new SymbolicIntValue("MEDIUM");
        SymbolicValue highestLoad = new SymbolicIntValue("HIGH");
        SymbolicValue timeToHighestLoad = new SymbolicIntValue("SLOW");
        SymbolicValue constantDuration = new SymbolicIntValue("FAST");

        LoadProfile loadProfile = new LoadIncrease(baseLoad, highestLoad, timeToHighestLoad, constantDuration);
        Workload workload = new OpenWorkload(loadProfile);
        int accuracy = 100;

        Stimulus stimulus = new Stimulus(workload, accuracy);
        return  stimulus;
    }

    private static Endpoint createEndpointModel() {
        String field = "/api/users/{id}";
        String operation = "POST";
        Map<String, String> pathVariables = Map.of("paths", "loadtest/paths.json");
        Map<String, String> urlParameter = Map.of("query", "loadtest/queryParams.json");
        Map<String, String> requestParameter = Map.of("headers", "loadtest/headers.json");
        Map<String, String> payload = Map.of("users", "loadtest/payload.json");
        LinkedHashSet<Response> responses = new LinkedHashSet<>();
        Response response = new Response(201, null);
        responses.add(response);

        Endpoint endpoint = new Endpoint(field, operation, pathVariables, urlParameter, requestParameter, payload, responses);
        return endpoint;
    }
}
