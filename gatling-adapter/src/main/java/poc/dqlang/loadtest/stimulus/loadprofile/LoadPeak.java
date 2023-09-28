package poc.dqlang.loadtest.stimulus.loadprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
public class LoadPeak extends LoadProfile {

    /**
     * Type identifier for JSON serialization.
     */
    public static final String TYPE_IDENTIFIER = "peak";

    @JsonProperty("peak_load")
    private SymbolicValue peakLoad;
    private SymbolicValue duration;
}
