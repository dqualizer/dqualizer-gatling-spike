package poc.dqlang.gatling.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GatlingAction {

    private String name;

    private GatlingRequest request;

    private GatlingParams params;

    private GatlingChecks checks;
}
