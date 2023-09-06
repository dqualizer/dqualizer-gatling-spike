package poc.dqlang.constants;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Technical {

    @JsonProperty("warm_up_duration")
    private int warmUpDuration;
    @JsonProperty("cool_down_duration")
    private int coolDownDuration;
    @JsonProperty("think_time")
    private int thinkTime;
}
