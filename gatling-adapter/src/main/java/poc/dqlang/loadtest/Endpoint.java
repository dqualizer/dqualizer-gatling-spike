package poc.dqlang.loadtest;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {

    private String field;
    private String operation;
    @JsonProperty("path_variables")
    private String pathVariables;
    @JsonProperty("url_parameter")
    private String urlParameter;
    @JsonProperty("request_parameter")
    private String requestParameter;
    private String payload;
    private LinkedHashSet<Response> responses;
}
