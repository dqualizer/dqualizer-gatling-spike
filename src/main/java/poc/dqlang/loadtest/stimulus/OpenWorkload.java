package poc.dqlang.loadtest.stimulus;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

@Getter
@ToString
public class OpenWorkload extends Workload {

    public OpenWorkload(LoadProfile loadProfile) {
        this.loadProfile = loadProfile;
    }
}
