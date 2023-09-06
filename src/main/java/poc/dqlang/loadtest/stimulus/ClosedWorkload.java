package poc.dqlang.loadtest.stimulus;

import lombok.Getter;
import lombok.ToString;
import poc.dqlang.loadtest.stimulus.symbolic.SymbolicValue;

@Getter
@ToString
public class ClosedWorkload extends Workload {

    public ClosedWorkload(LoadProfile loadProfile) {
        this.loadProfile = loadProfile;
    }
}
