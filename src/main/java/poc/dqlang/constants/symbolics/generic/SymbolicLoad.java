package poc.dqlang.constants.symbolics.generic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

@Getter
@ToString
public class SymbolicLoad<T> {

    private T low;
    private T medium;
    private T high;

    /**
     * TimeUnit for open loads,
     * @example users per SECONDS, users per MINUTES
     */
    @JsonProperty("time_unit")
    private TimeUnit timeUnit;
}
