package poc.dqlang.constants.symbolics.generic;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SymbolicType<T> {

    private SymbolicLoad<T> load;
    private SymbolicDuration<T> duration;
}
