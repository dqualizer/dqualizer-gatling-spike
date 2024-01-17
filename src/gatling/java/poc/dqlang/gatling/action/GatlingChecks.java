package poc.dqlang.gatling.action;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class GatlingChecks {

    private Set<Integer> statusCodes;
}
