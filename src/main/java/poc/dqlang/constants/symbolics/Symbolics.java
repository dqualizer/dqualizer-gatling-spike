package poc.dqlang.constants.symbolics;

import lombok.Getter;
import lombok.ToString;
import poc.dqlang.constants.symbolics.generic.SymbolicType;

@Getter
@ToString
public class Symbolics {

    private SymbolicType<Integer> integer;
    private SymbolicType<Double> decimal;
}
