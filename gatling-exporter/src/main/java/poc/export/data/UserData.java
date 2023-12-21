package poc.export.data;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableLongPointData;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static poc.export.data.GatlingSimulationType.USER;

@Getter
@ToString
public class UserData extends DataObject {
    public enum UserType { START, END }

    private final String name;
    private final UserType type;
    private final Long timestamp;

    public UserData(String[] simulationLine) {
        this.name = simulationLine[1];
        this.type = UserType.valueOf(simulationLine[2]);
        Long rawTimestamp = Long.valueOf(simulationLine[3]);
        this.timestamp = getFixedTimestamp(rawTimestamp);
    }

    @Override
    public LongPointData createCountData(long counter) {
        Attributes attributes = Attributes.of(
                stringKey("type"), USER.name(),
                stringKey("user.name"), this.name,
                stringKey("service.name"), SERVICE_NAME
        );
        return ImmutableLongPointData.create(
                this.timestamp,
                this.timestamp,
                attributes,
                counter
        );
    }
}
