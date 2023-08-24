package poc.dqlang.stimulus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import poc.dqlang.stimulus.symbolic.SymbolicValue;

@Getter
@ToString
public abstract class LoadProfile {

    @JsonProperty("base_load")
    SymbolicValue baseLoad;
}
