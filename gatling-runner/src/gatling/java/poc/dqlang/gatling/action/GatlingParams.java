package poc.dqlang.gatling.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GatlingParams {

    private String requestParams;

    private String payload;

    private String pathVariables;

    private String queryParams;
}
