package poc.export.data;

import io.opentelemetry.sdk.metrics.data.LongPointData;

import java.util.concurrent.TimeUnit;

public abstract class DataObject {

    /**
     * @return A metric created from the data stored in the current data object
     */
    public abstract LongPointData createPointData();

    Long getFixedTimestamp(Long timestamp) {
        return TimeUnit.MILLISECONDS.toNanos(timestamp);
    }
}
