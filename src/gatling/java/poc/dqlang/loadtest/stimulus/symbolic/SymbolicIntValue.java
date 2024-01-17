package poc.dqlang.loadtest.stimulus.symbolic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class SymbolicIntValue extends SymbolicValue {

    /**
     * Type identifier for JSON serialization.
     */
    public static final String TYPE_IDENTIFIER = "integer";

    @Setter
    private int value;
}
