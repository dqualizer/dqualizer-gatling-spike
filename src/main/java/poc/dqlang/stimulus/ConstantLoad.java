package poc.dqlang.stimulus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import poc.dqlang.stimulus.symbolic.SymbolicValue;

@Getter
@ToString
public class ConstantLoad extends LoadProfile {

    @JsonProperty("target_load")
    private SymbolicValue targetLoad;
}
