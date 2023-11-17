package poc.export.data;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableLongPointData;

import java.util.concurrent.TimeUnit;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static poc.export.data.GatlingSimulationType.USER;

public class UserData extends DataObject {
    private final String name;
    private final String type; // Either START or END
    private final Long time;

    public UserData(String[] simulationLine) {
        this.name = simulationLine[1];
        this.type = simulationLine[2];
        this.time = Long.valueOf(simulationLine[3]);
    }

    @Override
    public LongPointData createPointData() {
        return null;
    }

    public LongPointData createCounterData(long counter) {
        Long timestamp = getFixedTimestamp(time);
        Attributes attributes = Attributes.of(
                stringKey("type"), USER.name(),
                stringKey("name"), this.name
        );
        return ImmutableLongPointData.create(
                timestamp,
                timestamp,
                attributes,
                counter
        );
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "UserData {" +
                "name = '" + name + '\'' +
                ", type = '" + type + '\'' +
                ", time = " + time +
                '}';
    }
}
