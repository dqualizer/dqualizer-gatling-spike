package poc.dqlang;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseMeasure {

    @JsonProperty("response_time")
    private String responseTime;
}
