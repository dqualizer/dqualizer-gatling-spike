package poc.export.data;

import io.opentelemetry.sdk.metrics.data.LongPointData;

public interface DataObject {

    /**
     * @return A metric created from the data stored in the current data object
     */
    LongPointData createPointData();
}
