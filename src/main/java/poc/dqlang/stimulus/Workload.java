package poc.dqlang.stimulus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class Workload {

    @JsonProperty("load_profile")
    private LoadProfile loadProfile;
}
