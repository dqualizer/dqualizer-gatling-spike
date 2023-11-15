package poc.export.data;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableLongPointData;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static poc.export.data.GatlingSimulationType.REQUEST;

public class RequestData implements DataObject {

    private final String name;
    private final Long startTime;
    private final Long endTime;
    private final String status;

    public RequestData(String[] simulationLine) {
        this.name = simulationLine[2];
        this.startTime = Long.valueOf(simulationLine[3]);
        this.endTime = Long.valueOf(simulationLine[4]);
        //Long startTimestamp = Long.parseLong(simulationLine[3]);
        //this.startTime = TimeUnit.SECONDS.toNanos(startTimestamp);
        //Long endTimestamp = Long.parseLong(simulationLine[4]);
        //.endTime = TimeUnit.SECONDS.toNanos(endTimestamp);
        this.status = simulationLine[5];
    }

    @Override
    public LongPointData createPointData() {
        Attributes attributes = Attributes.of(stringKey("type"), REQUEST.name());
        Long duration = getDuration();
        return ImmutableLongPointData.create(
                startTime,
                endTime,
                attributes,
                duration
        );//.create(startTime, endTime, attributes, getDuration());

    }

    public String getName() {
        return this.name;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public Long getEndTime() {
        return this.endTime;
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
