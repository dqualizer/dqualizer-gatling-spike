package poc.dqlang.gatling.stimulus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatlingInjectionPeakProfile extends GatlingInjectionProfile {

    private int peakLoad;

    private int duration;

    public GatlingInjectionPeakProfile(double baseLoad, int peakLoad, int duration) {
        super(baseLoad);
        this.peakLoad = peakLoad;
        this.duration = duration;
    }
}
