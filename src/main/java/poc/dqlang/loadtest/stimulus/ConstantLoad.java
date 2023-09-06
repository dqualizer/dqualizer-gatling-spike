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
public class ConstantLoad extends LoadProfile {

    @JsonProperty("target_load")
    private SymbolicValue targetLoad;
    private SymbolicValue duration;

    public ConstantLoad(SymbolicValue baseLoad, SymbolicValue targetLoad, SymbolicValue duration) {
        this.baseLoad = baseLoad;
        this.targetLoad = targetLoad;
        this.duration = duration;
    }
}
