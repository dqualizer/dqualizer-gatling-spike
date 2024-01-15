package poc.dqlang.gatling.stimulus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatlingInjectionConstantProfile extends GatlingInjectionProfile {

    private double targetLoad;

    private int duration;

    public GatlingInjectionConstantProfile(double baseLoad, double targetLoad, int duration) {
        super(baseLoad);
        this.targetLoad = targetLoad;
        this.duration = duration;
    }
}
