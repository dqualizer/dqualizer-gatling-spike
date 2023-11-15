package poc.export.data;

import io.opentelemetry.sdk.metrics.data.LongPointData;

public class UserData implements DataObject {
    private final String name;
    private final String type;
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

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public Long getStartTime() {
        return this.time;
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
