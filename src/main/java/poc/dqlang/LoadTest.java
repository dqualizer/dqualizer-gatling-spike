package poc.dqlang;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import poc.dqlang.stimulus.Stimulus;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoadTest {

    private Artifact artifact;
    private String description;
    private Stimulus stimulus;
    @JsonProperty("response_measure")
    private ResponseMeasure responseMeasure;
    private Endpoint endpoint;
}
