package poc.dqlang.gatling.action;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatlingRequest {

    private String method;

    private String path;
}
