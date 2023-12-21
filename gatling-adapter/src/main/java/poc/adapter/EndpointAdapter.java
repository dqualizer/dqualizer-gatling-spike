package poc.adapter;

import org.springframework.stereotype.Component;
import poc.dqlang.loadtest.Endpoint;
import poc.dqlang.loadtest.LoadTestArtifact;
import poc.dqlang.loadtest.Response;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EndpointAdapter {

    private final static String newLine = System.lineSeparator();

    public String adapt(LoadTestArtifact loadTest) {
        StringBuilder builder = new StringBuilder();
        builder.append("actions  = [" + newLine);
        // TODO Use for-loop to allow multiple endpoints
        builder.append(" {" + newLine);

        String name = this.getName(loadTest.getDescription());
        builder.append(name);

        Endpoint endpoint = loadTest.getEndpoint();
        String request = this.getRequestConfig(endpoint);
        builder.append(request);

        String params = this.getParamsConfig(endpoint);
        builder.append(params);

        String checks = this.getChecksConfig(endpoint);
        builder.append(checks);

        builder.append(" }" + newLine);
        builder.append("]" + newLine);
        return builder.toString();
    }

    private String getName(String name) {
        return "name = " + "\"" + name + "\"" + newLine;
    }

    private String getRequestConfig(Endpoint endpoint) {
        StringBuilder builder = new StringBuilder();
        builder.append("request {" + newLine);

        String method = endpoint.getOperation();
        builder.append("method = \"" + method + "\"" + newLine);

        String path = this.markPathVariables(endpoint.getField());
        builder.append("path = \"" + path + "\"" + newLine);

        builder.append("}" + newLine);
        return builder.toString();
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

    private String getParamsConfig(Endpoint endpoint) {
        StringBuilder builder = new StringBuilder();
        builder.append("params {" + newLine);

        String requestParams = endpoint.getRequestParameter();
        builder.append("requestParams = \"" + requestParams + "\"" + newLine);

        String payload = endpoint.getPayload();
        builder.append("payload = \"" + payload + "\"" + newLine);

        String pathVariables = endpoint.getPathVariables();
        builder.append("pathVariables = \"" + pathVariables + "\"" + newLine);

        String queryParams = endpoint.getUrlParameter();
        builder.append("queryParams = \"" + queryParams + "\"" + newLine);

        builder.append("}" + newLine);
        return builder.toString();
    }

    private String getChecksConfig(Endpoint endpoint) {
        StringBuilder builder = new StringBuilder();
        builder.append("checks {" + newLine);
        LinkedList<String > statusCodes = new LinkedList<>();

        LinkedHashSet<Response> responses = endpoint.getResponses();
        responses.stream().forEach(response -> {
            Integer statusCode = response.getExpectedCode();
            if(statusCode != null) statusCodes.add(String.valueOf(statusCode));
            //Get more information, if needed
        });

        String joinedStatusCodes = String.join(", ", statusCodes);
        builder.append("statusCodes = [" + joinedStatusCodes + "]" + newLine);
        // Add more information, if needed

        builder.append("}" + newLine);
        return builder.toString();
    }
}
