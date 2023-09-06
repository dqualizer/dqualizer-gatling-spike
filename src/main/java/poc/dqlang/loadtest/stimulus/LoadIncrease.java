package poc.dqlang.loadtest.stimulus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicValue;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoadIncrease extends LoadProfile {

    @JsonProperty("highest_load")
    private SymbolicValue highestLoad;

    @JsonProperty("time_to_highest_load")
    private SymbolicValue timeToHighestLoad;

    @JsonProperty("constant_duration")
    private SymbolicValue constantDuration;

    public LoadIncrease(SymbolicValue baseLoad, SymbolicValue highestLoad, SymbolicValue timeToHighestLoad, SymbolicValue constantDuration) {
        this.baseLoad = baseLoad;
        this.highestLoad = highestLoad;
        this.timeToHighestLoad = timeToHighestLoad;
        this.constantDuration = constantDuration;
    }
}
