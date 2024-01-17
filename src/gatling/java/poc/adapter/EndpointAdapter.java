package poc.adapter;

import org.springframework.stereotype.Component;
import poc.dqlang.gatling.action.GatlingAction;
import poc.dqlang.gatling.action.GatlingChecks;
import poc.dqlang.gatling.action.GatlingParams;
import poc.dqlang.gatling.action.GatlingRequest;
import poc.dqlang.loadtest.Endpoint;
import poc.dqlang.loadtest.LoadTestArtifact;
import poc.dqlang.loadtest.Response;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EndpointAdapter {

    public List<GatlingAction> adapt(LoadTestArtifact loadTest) {
        String name = loadTest.getDescription();
        List<GatlingAction> actions = new LinkedList<>();

        // TODO Use for-loop to allow multiple endpoints
        Endpoint endpoint = loadTest.getEndpoint();

        String method = endpoint.getOperation();
        String path = this.markPathVariables(endpoint.getField());
        GatlingRequest request = new GatlingRequest(method, path);

        GatlingParams params = new GatlingParams(
                endpoint.getRequestParameter(),
                endpoint.getPayload(),
                endpoint.getPathVariables(),
                endpoint.getUrlParameter()
        );

        GatlingChecks checks = this.getChecks(endpoint);

        GatlingAction action = GatlingAction.builder()
                .name(name)
                .request(request)
                .params(params)
                .checks(checks)
                .build();
        actions.add(action);

        return actions;
    }

    private GatlingChecks getChecks(Endpoint endpoint) {
        Set<Integer> statusCodes = new HashSet<>();
        LinkedHashSet<Response> responses = endpoint.getResponses();

        responses.forEach(response -> {
            Integer statusCode = response.getExpectedCode();
            if(statusCode != null) statusCodes.add(statusCode);
            // get more information, if needed
        });

        return new GatlingChecks(statusCodes);
    }

    private String markPathVariables(String field) {
        Pattern pattern = Pattern.compile("\\{.*?}");
        Matcher matcher = pattern.matcher(field);
        List<String> variables = new LinkedList<>();

        while (matcher.find()) {
            String foundVariable = matcher.group();
            variables.add(foundVariable);
        }

        String markedPathVariables = variables.stream()
                .distinct()
                .reduce(field, (path, variable) -> path.replace(variable, "$"+ variable));
        return markedPathVariables;
    }
}
