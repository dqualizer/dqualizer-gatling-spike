package poc.dqlang.gatling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import poc.dqlang.gatling.action.GatlingAction;
import poc.dqlang.gatling.stimulus.GatlingStimulus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GatlingLoadTest {

    private int repetition;

    private GatlingStimulus stimulus;

    private List<GatlingAction> actions;
}
