package poc.export;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableGaugeData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableLongPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableMetricData;
import io.opentelemetry.sdk.resources.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import poc.exception.UnknownTypeException;
import poc.export.data.RequestData;
import poc.export.data.RequestData.StatusType;
import poc.export.data.SimulationData;
import poc.export.data.UserData;
import poc.export.data.UserData.UserType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static poc.export.data.DataObject.SERVICE_NAME;
import static poc.export.data.GatlingSimulationType.*;

@Slf4j
@Component
public class MetricBuilder {

    private static final String SIMULATION_COUNT = "simulation_count";
    private static final String USER_COUNT = "user_count";
    private static final String REQUEST_COUNT = "request_count";
    private static final String REQUEST_FAILURE_COUNT = "request_failure_count";
    private static final String REQUEST_DURATION = "request_duration";
    private static final String REQUEST_FAILURE_RATE = "request_failure_rate";
    private static final String TOTAL_RUNTIME_DURATION = "total_runtime_duration";

    private SimulationData simulationData;

    public List<MetricData> buildMetrics(List<String[]> records) {
        Map<String, List<LongPointData>> pointData = new HashMap<>();

        AtomicLong userCounter = new AtomicLong(0L);
        AtomicLong requestCounter = new AtomicLong(0L);
        AtomicLong requestFailureCounter = new AtomicLong(0L);

        AtomicReference<Long> latestUserTimestamp = new AtomicReference<>(0L);

        records.forEach(line -> {
            String type = line[0];

            if(type.equals(RUN.name())) {
                // There should be only one line with RUN type per simulation
                simulationData = new SimulationData(line);
                LongPointData countPoint = simulationData.createCountData(1);
                pointData.computeIfAbsent(SIMULATION_COUNT, key -> new LinkedList<>()).add(countPoint);
            }
            else if(type.equals(USER.name())) {
                UserData userData = new UserData(line);
                if(userData.getType().equals(UserType.START)) userCounter.getAndIncrement();
                else userCounter.getAndDecrement();

                LongPointData countPoint = userData.createCountData(userCounter.get());
                pointData.computeIfAbsent(USER_COUNT, key -> new LinkedList<>()).add(countPoint);
                latestUserTimestamp.set(userData.getTimestamp());
            }
            else if(type.equals(REQUEST.name())) {
                RequestData requestData = new RequestData(line);
                requestCounter.getAndIncrement();

                LongPointData countPoint = requestData.createCountData(requestCounter.get());
                LongPointData durationPoint = requestData.createDurationData();
                pointData.computeIfAbsent(REQUEST_COUNT, key -> new LinkedList<>()).add(countPoint);
                pointData.computeIfAbsent(REQUEST_DURATION, key -> new LinkedList<>()).add(durationPoint);

                if(requestData.getStatus().equals(StatusType.KO)) {
                    requestFailureCounter.getAndIncrement();
                    LongPointData failureCountPoint = requestData.createCountData(requestFailureCounter.get());
                    pointData.computeIfAbsent(REQUEST_FAILURE_COUNT, key -> new LinkedList<>()).add(failureCountPoint);
                }
            }
            else throw new UnknownTypeException(type);
        });

        LongPointData failureRate = createRequestFailureRateData(requestCounter.get(), requestFailureCounter.get());
        pointData.computeIfAbsent(REQUEST_FAILURE_RATE, key -> new LinkedList<>()).add(failureRate);

        LongPointData totalRuntime = createTotalRuntimeData(simulationData, latestUserTimestamp.get());
        pointData.computeIfAbsent(TOTAL_RUNTIME_DURATION, key -> new LinkedList<>()).add(totalRuntime);

        return createMetrics(pointData);
    }

    /**
     * Creates one point data with the request failure rate of the simulation
     * @param requestCount total amount of request
     * @param failureCount amount of failed requests
     * @return Point data with request failure rate in percent
     */
    private LongPointData createRequestFailureRateData(Long requestCount, Long failureCount) {
        long currentTimestamp = TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis());
        String name = simulationData.getName();
        Attributes attributes = Attributes.of(
                stringKey("simulation.name"), name,
                stringKey("service.name"), SERVICE_NAME
        );

        long failureRate = (long) ((failureCount.doubleValue() / requestCount) * 100);

        return ImmutableLongPointData.create(
                currentTimestamp,
                currentTimestamp,
                attributes,
                failureRate
        );
    }

    /**
     * Creates one point data with the calculated total runtime of the simulation
     * @param simulationData Simulation data, including name and start timestamp in milliseconds
     * @param latestUserTimestamp Latest user timestamp in nanoseconds
     * @return Point data with total runtime in milliseconds
     */
    private LongPointData createTotalRuntimeData(SimulationData simulationData, Long latestUserTimestamp) {
        long currentTimestamp = TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis());
        String name = simulationData.getName();
        long startTimestamp = simulationData.getStartTimestamp();
        Attributes attributes = Attributes.of(
                stringKey("simulation.name"), name,
                stringKey("service.name"), SERVICE_NAME
        );

        // User timestamp has to be converted to milliseconds
        long convertedUserTimestamp = TimeUnit.NANOSECONDS.toMillis(latestUserTimestamp);
        long totalRuntime = (convertedUserTimestamp - startTimestamp);

        return ImmutableLongPointData.create(
                currentTimestamp,
                currentTimestamp,
                attributes,
                totalRuntime
        );
    }

    /**
     * Creates a list of OpenTelemetry metrics out of the proviced point data
     * @param pointData List of data points for specific metrics
     * @return List of OpenTelemetry metric data
     */
    private List<MetricData> createMetrics(Map<String, List<LongPointData>> pointData) {
        List<MetricData> metricData = new LinkedList<>();

        pointData.forEach((name, data) -> {
            String unit = getUnit(name);
            MetricData metric = ImmutableMetricData.createLongGauge(
                    Resource.getDefault(),
                    InstrumentationScopeInfo.create("github.io.dqualizer.gatling"),
                    name,
                    "Gatling Metric",
                    unit,
                    ImmutableGaugeData.create(data)
            );
            metricData.add(metric);
        });

        return metricData;
    }

    /**
     * Determines the unit based on the metric name ending
     * @param name metric name
     * @return unit for the provided metric
     */
    private String getUnit(String name) {
        if (name.endsWith("rate")) return "%";
        else if (name.endsWith("duration")) return "ms";
        else return "1";
    }
}
