package poc.dqlang.loadtest.stimulus.loadprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicValue;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class LoadIncrease extends LoadProfile {

    /**
     * Type identifier for JSON serialization.
     */
    public static final String TYPE_IDENTIFIER = "increase";

    @JsonProperty("highest_load")
    private SymbolicValue highestLoad;

    @JsonProperty("time_to_highest_load")
    private SymbolicValue timeToHighestLoad;

    @JsonProperty("constant_duration")
    private SymbolicValue constantDuration;
}
