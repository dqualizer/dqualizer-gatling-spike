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
public class LoadPeak extends LoadProfile {

    @JsonProperty("peak_load")
    private SymbolicValue peakLoad;
    private SymbolicValue duration;

    public LoadPeak(SymbolicValue baseLoad, SymbolicValue peakLoad, SymbolicValue duration) {
        this.baseLoad = baseLoad;
        this.peakLoad = peakLoad;
        this.duration = duration;
    }
}
