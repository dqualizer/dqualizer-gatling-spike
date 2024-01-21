package poc.dqlang.constants;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import poc.dqlang.constants.accuracy.Accuracy;
import poc.dqlang.constants.symbolics.Symbolics;

/**
 * Java class for load test constants definitions
 */
@Getter
@ToString
public class LoadTestConstants {

    private Accuracy accuracy;
    @JsonProperty("technical_constants")
    private TechnicalConstants technicalConstants;
    private Symbolics symbolics;
    @JsonProperty("response_time")
    private ResponseTime responseTime;
}
