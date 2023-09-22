package poc.dqlang.loadtest.stimulus.loadprofile;

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
public abstract class LoadProfile {

    @JsonProperty("base_load")
    SymbolicValue baseLoad;
}
