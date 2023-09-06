package poc.dqlang.constants.symbolics.generic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

@Getter
@ToString
public class SymbolicDuration<T> {

    private T slow;
    private T fast;
    @JsonProperty("very_fast")
    private T veryFast;
    @JsonProperty("time_unit")
    private TimeUnit timeUnit;
}
