package poc.dqlang.stimulus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import poc.dqlang.stimulus.symbolic.SymbolicValue;

@Getter
@ToString
public class LoadIncrease extends LoadProfile {

    @JsonProperty("highest_load")
    private SymbolicValue highestLoad;

    @JsonProperty("time_to_highest_load")
    private SymbolicValue timeToHighestLoad;

    @JsonProperty("constant_duration")
    private SymbolicValue constantDuration;
}
