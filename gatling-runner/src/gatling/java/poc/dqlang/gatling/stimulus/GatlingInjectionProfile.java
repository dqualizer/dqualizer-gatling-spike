package poc.dqlang.gatling.stimulus;

import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
public abstract class GatlingInjectionProfile {

    private double baseLoad;
}
