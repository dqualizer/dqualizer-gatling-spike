package poc.dqlang.constants.symbolics;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import poc.dqlang.constants.symbolics.generic.SymbolicDurationType;

import java.util.concurrent.TimeUnit;

@Getter
@ToString
public class SymbolicDuration {

    private SymbolicDurationType<Integer> integer;
    private SymbolicDurationType<Double> decimal;

    @JsonProperty("time_unit")
    private TimeUnit timeUnit;
}
