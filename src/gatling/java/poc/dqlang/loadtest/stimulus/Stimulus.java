package poc.dqlang.loadtest.stimulus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Stimulus {

    private Workload workload;
    private int accuracy;
}
