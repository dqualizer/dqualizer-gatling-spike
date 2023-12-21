package poc.export.data;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableLongPointData;
import lombok.Getter;
import lombok.ToString;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static poc.export.data.GatlingSimulationType.REQUEST;

@Getter
@ToString
public class RequestData extends DataObject {
    // I assume, these are the only two status types. There is no documentation.
    public enum StatusType { OK, KO }

    private final String name;
    /** Timestamp defined by Gatling */
    private final Long rawStartTimestamp;
    /** Fixed timestamp for OpenTelemetry */
    private final Long startTimestamp;
    /** Timestamp defined by Gatling */
    private final Long rawEndTimestamp;
    /** Fixed timestamp for OpenTelemetry */
    private final Long endTimestamp;
    private final StatusType status;
    private final String exception;

    public RequestData(String[] simulationLine) {
        this.name = simulationLine[2];
        this.rawStartTimestamp = Long.parseLong(simulationLine[3]);
        this.startTimestamp = getFixedTimestamp(rawStartTimestamp);
        this.rawEndTimestamp = Long.parseLong(simulationLine[4]);
        this.endTimestamp = getFixedTimestamp(rawEndTimestamp);
        this.status = StatusType.valueOf(simulationLine[5]);
        this.exception = simulationLine[6];
    }

    @Override
    public LongPointData createCountData(long counter) {
        Attributes attributes = Attributes.of(
                stringKey("type"), REQUEST.name(),
                stringKey("request.name"), this.name,
                stringKey("status"), this.status.name(),
                stringKey("exception"), this.exception,
                stringKey("service.name"), SERVICE_NAME
        );
        return ImmutableLongPointData.create(
                startTimestamp,
                endTimestamp,
                attributes,
                counter
        );
    }

    public LongPointData createDurationData() {
        Attributes attributes = Attributes.of(
                stringKey("type"), REQUEST.name(),
                stringKey("request.name"), this.name,
                stringKey("status"), this.status.name(),
                stringKey("exception"), this.exception,
                stringKey("service.name"), SERVICE_NAME
        );
        Long duration = getDuration();
        return ImmutableLongPointData.create(
                startTimestamp,
                endTimestamp,
                attributes,
                duration
        );
    }

    private Long getDuration() {
        return this.rawEndTimestamp - this.rawStartTimestamp;
    }
}
