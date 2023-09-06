package poc.dqlang.loadtest.stimulus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public abstract class Workload {

    //TODO Die abstrake Klasse kann man sich sparen, stattdessen eine Property type einführen (open || closed)

    @JsonProperty("load_profile")
    LoadProfile loadProfile;
}
