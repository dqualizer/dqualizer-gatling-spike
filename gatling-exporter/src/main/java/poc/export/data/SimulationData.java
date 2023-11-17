package poc.export.data;

import io.opentelemetry.sdk.metrics.data.LongPointData;

public class SimulationData extends DataObject {
    private final String name;
    private final Long startTime;
    private final String gatlingVersion;

    public SimulationData(String[] simulationLine) {
        this.name = simulationLine[2];
        this.startTime = Long.valueOf(simulationLine[3]);
        this.gatlingVersion = simulationLine[4];
    }

    @Override
    public LongPointData createPointData() {
        return null;
    }

    public String getName() {
        return this.name;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public String getGatlingVersion() {
        return this.gatlingVersion;
    }

    @Override
    public String toString() {
        return "SimulationData {" +
                "name = '" + name + '\'' +
                ", startTime = " + startTime +
                ", gatlingVersion = '" + gatlingVersion + '\'' +
                '}';
    }
}
