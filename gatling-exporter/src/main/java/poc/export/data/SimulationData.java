package poc.export.data;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableLongPointData;
import lombok.Getter;
import lombok.ToString;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static poc.export.data.GatlingSimulationType.RUN;

@Getter
@ToString
public class SimulationData extends DataObject {
    private final String name;
    /** Timestamp for the simulation start in milliseconds */
    private final Long startTimestamp;
    private final String gatlingVersion;

    public SimulationData(String[] simulationLine) {
        this.name = simulationLine[2];
        this.startTimestamp = Long.valueOf(simulationLine[3]);
        this.gatlingVersion = simulationLine[4];
    }

    @Override
    public LongPointData createCountData(long counter) {
        Attributes attributes = Attributes.of(
                stringKey("type"), RUN.name(),
                stringKey("name"), this.name,
                stringKey("gatling.version"), this.gatlingVersion,
                stringKey("service.name"), SERVICE_NAME
        );
        return ImmutableLongPointData.create(
                startTimestamp,
                startTimestamp,
                attributes,
                counter
        );
    }
}
