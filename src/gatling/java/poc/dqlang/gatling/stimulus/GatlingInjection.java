package poc.dqlang.gatling.stimulus;

import lombok.AllArgsConstructor;
import lombok.Data;
import poc.dqlang.loadtest.stimulus.WorkloadType;

@Data
@AllArgsConstructor
public class GatlingInjection {

    private WorkloadType workloadType;

    private GatlingInjectionProfile profile;
}
