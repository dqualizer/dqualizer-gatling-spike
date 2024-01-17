package poc.dqlang.loadtest.stimulus.loadprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicValue;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class ConstantLoad extends LoadProfile {

    /**
     * Type identifier for JSON serialization.
     */
    public static final String TYPE_IDENTIFIER = "constant";

    @JsonProperty("target_load")
    private SymbolicValue targetLoad;
    private SymbolicValue duration;
}
