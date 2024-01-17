package poc.dqlang.gatling.stimulus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatlingInjectionIncreaseProfile extends GatlingInjectionProfile {

    private double highestLoad;

    private int timeToHighestLoad;

    private int constantDuration;

    public GatlingInjectionIncreaseProfile(double baseLoad, double highestLoad, int timeToHighestLoad, int constantDuration) {
        super(baseLoad);
        this.highestLoad = highestLoad;
        this.timeToHighestLoad = timeToHighestLoad;
        this.constantDuration = constantDuration;
    }
}
