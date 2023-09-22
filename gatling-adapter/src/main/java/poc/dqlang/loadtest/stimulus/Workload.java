package poc.dqlang.loadtest.stimulus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import poc.dqlang.loadtest.stimulus.loadprofile.LoadProfile;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Workload {

    private WorkloadType type;

    @JsonProperty("load_profile")
    private LoadProfile loadProfile;
}
