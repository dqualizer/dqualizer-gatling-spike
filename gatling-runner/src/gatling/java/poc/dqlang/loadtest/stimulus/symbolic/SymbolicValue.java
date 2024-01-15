package poc.dqlang.loadtest.stimulus.symbolic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = SymbolicIntValue.TYPE_IDENTIFIER, value = SymbolicIntValue.class),
        @JsonSubTypes.Type(name = SymbolicDoubleValue.TYPE_IDENTIFIER, value = SymbolicDoubleValue.class),
})
public abstract class SymbolicValue {
    String name;
}
