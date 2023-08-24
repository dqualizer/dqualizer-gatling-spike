package poc.dqlang;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Map;

@Getter
@ToString
public class Endpoint {

    private String field;
    private String operation;
    @JsonProperty("path_variables")
    private Map<String, String> pathVariables;
    @JsonProperty("url_parameter")
    private Map<String, String> urlParameter; //Size should be 1
    @JsonProperty("request_parameter")
    private Map<String, String> requestParameter; //Size should be 1
    private Map<String, String> payload; //Size should be 1
    private LinkedHashSet<Response> responses;
}
