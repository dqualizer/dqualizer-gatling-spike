package poc.dqlang.loadtest.stimulus.symbolic;

import lombok.Getter;;
import lombok.Setter;
import lombok.ToString;
import poc.dqlang.constants.GatlingConstants;
import poc.dqlang.constants.symbolics.generic.SymbolicType;
import poc.util.ConstantsLoader;

@Getter
@Setter
@ToString
public class SymbolicIntValue extends SymbolicValue {

    @Setter
    private int value;

    public SymbolicIntValue(String name) {
        this.name = name;
    }
}
