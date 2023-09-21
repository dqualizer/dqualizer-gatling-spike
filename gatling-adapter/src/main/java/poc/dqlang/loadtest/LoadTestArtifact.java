package poc.dqlang.loadtest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import poc.dqlang.loadtest.stimulus.Stimulus;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoadTestArtifact {

    private Artifact artifact;
    private String description;
    private Stimulus stimulus;
    @JsonProperty("response_measure")
    private ResponseMeasure responseMeasure;
    private Endpoint endpoint;
}
