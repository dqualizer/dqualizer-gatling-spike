package poc.util;

import poc.dqlang.loadtest.*;
import poc.dqlang.loadtest.stimulus.Stimulus;
import poc.dqlang.loadtest.stimulus.Workload;
import poc.dqlang.loadtest.stimulus.WorkloadType;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadIncrease;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadProfile;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicIntValue;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicValue;

import java.util.LinkedHashSet;

public class ConfigModelCreator {

    public static LoadTestConfiguration create() {
        LinkedHashSet<LoadTestArtifact> loadTests = new LinkedHashSet<>();
        LoadTestArtifact oneLoadTest = createLoadTestModel("First dqTest");
        LoadTestArtifact anotherLoadTest = createLoadTestModel("Second dqTest");
        loadTests.add(oneLoadTest);
        //loadTests.add(anotherLoadTest);

        int version = 1;
        String context = "gatling-test";
        String environment = "dev";
        String baseURL = "https://reqres.in";

        LoadTestConfiguration config = new LoadTestConfiguration(version, context, environment, baseURL, loadTests);
        return config;
    }

    private static LoadTestArtifact createLoadTestModel(String name) {
        Artifact artifact = new Artifact();
        ResponseMeasure responseMeasure = new ResponseMeasure();

        Stimulus stimulus = createStimulusModel();
        Endpoint endpoint = createEndpointModel();

        LoadTestArtifact loadTest = new LoadTestArtifact(artifact, name, stimulus, responseMeasure, endpoint);
        return loadTest;
    }

    private static LoadTestArtifact createAnotherLoadTestModel(String name) {
        Artifact artifact = new Artifact();
        ResponseMeasure responseMeasure = new ResponseMeasure();

        Stimulus stimulus = createAnotherStimulusModel();
        Endpoint endpoint = createEndpointModel();

        LoadTestArtifact loadTest = new LoadTestArtifact(artifact, name, stimulus, responseMeasure, endpoint);
        return loadTest;
    }

    private static Stimulus createStimulusModel() {
        SymbolicValue baseLoad = SymbolicIntValue.builder().name("LOW").build();
        SymbolicValue highestLoad = SymbolicIntValue.builder().name("LOW").build();
        SymbolicValue timeToHighestLoad = SymbolicIntValue.builder().name("VERY_FAST").build();
        SymbolicValue constantDuration = SymbolicIntValue.builder().name("VERY_FAST").build();

        LoadProfile loadProfile = LoadIncrease.builder()
                .baseLoad(baseLoad)
                .highestLoad(highestLoad)
                .timeToHighestLoad(timeToHighestLoad)
                .constantDuration(constantDuration)
                .build();
        Workload workload = new Workload(WorkloadType.OPEN, loadProfile);
        int accuracy = 100;

        Stimulus stimulus = new Stimulus(workload, accuracy);
        return  stimulus;
    }

    private static Stimulus createAnotherStimulusModel() {
        SymbolicValue baseLoad = SymbolicIntValue.builder().name("LOW").build();
        SymbolicValue highestLoad = SymbolicIntValue.builder().name("LOW").build();
        SymbolicValue timeToHighestLoad = SymbolicIntValue.builder().name("FAST").build();
        SymbolicValue constantDuration = SymbolicIntValue.builder().name("SLOW").build();

        LoadProfile loadProfile = LoadIncrease.builder()
                .baseLoad(baseLoad)
                .highestLoad(highestLoad)
                .timeToHighestLoad(timeToHighestLoad)
                .constantDuration(constantDuration)
                .build();
        Workload workload = new Workload(WorkloadType.CLOSED, loadProfile);
        int accuracy = 800;

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
