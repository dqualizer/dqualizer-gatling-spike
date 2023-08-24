package poc.dqlang.stimulus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import poc.dqlang.stimulus.symbolic.SymbolicValue;

@Getter
@ToString
public class LoadPeak extends LoadProfile {

    @JsonProperty("peak_load")
    private SymbolicValue peakLoad;
}
