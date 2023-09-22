package poc.dqlang.loadtest.stimulus.symbolic;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SymbolicDoubleValue extends SymbolicValue {

    private double value;

    public SymbolicDoubleValue(String name) {
        this.name = name;
    }
}
