package poc.export.data;

import io.opentelemetry.sdk.metrics.data.LongPointData;

import java.util.concurrent.TimeUnit;

public abstract class DataObject {
    public static final String SERVICE_NAME = "dqualizer-gatling";

    /**
     * @return A metric created from the data stored in the current data object
     */
    public abstract LongPointData createCountData(long counter);

    Long getFixedTimestamp(Long timestamp) {
        return TimeUnit.MILLISECONDS.toNanos(timestamp);
    }
}
