package poc.export.data;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableLongPointData;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static poc.export.data.GatlingSimulationType.REQUEST;

public class RequestData extends DataObject {

    private final String name;
    private final Long startTime;
    private final Long endTime;
    private final String status;

    public RequestData(String[] simulationLine) {
        this.name = simulationLine[2];
        this.startTime = Long.parseLong(simulationLine[3]);
        this.endTime = Long.parseLong(simulationLine[4]);
        this.status = simulationLine[5];
    }

    @Override
    public LongPointData createPointData() {
        Long startTimestamp = getFixedTimestamp(startTime);
        Long endTimestamp = getFixedTimestamp(endTime);
        Attributes attributes = Attributes.of(
                stringKey("type"), REQUEST.name(),
                stringKey("name"), this.name,
                stringKey("status"), this.status
        );
        Long duration = getDuration();
        return ImmutableLongPointData.create(
                startTimestamp,
                endTimestamp,
                attributes,
                duration
        );
    }

    public LongPointData createCounterData(long counter) {
        Long startTimestamp = getFixedTimestamp(startTime);
        Long endTimestamp = getFixedTimestamp(endTime);
        Attributes attributes = Attributes.of(
                stringKey("type"), REQUEST.name(),
                stringKey("name"), this.name,
                stringKey("status"), this.status
        );
        return ImmutableLongPointData.create(
          startTimestamp,
          endTimestamp,
          attributes,
          counter
        );
    }

    public String getStatus() {
        return this.status;
    }

    private Long getDuration() {
        return this.endTime - this.startTime;
    }

    @Override
    public String toString() {
        return "RequestData {" +
                "name = '" + name + '\'' +
                ", startTime = " + startTime +
                ", endTime = " + endTime +
                ", status = '" + status + '\'' +
                '}';
    }
}
