package poc.dqlang.gatling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poc.dqlang.constants.TechnicalConstants;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatlingConfiguration {

    private String context;

    private String baseURL;

    private TechnicalConstants technicalConstants;

    private List<GatlingLoadTest> loadTests;
}
