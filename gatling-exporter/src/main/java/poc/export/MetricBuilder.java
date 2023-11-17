package poc.export;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.data.PointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableGaugeData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableLongPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableMetricData;
import io.opentelemetry.sdk.resources.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import poc.exception.UnknownTypeException;
import poc.export.data.GatlingSimulationType;
import poc.export.data.RequestData;
import poc.export.data.SimulationData;
import poc.export.data.UserData;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static poc.export.data.GatlingSimulationType.*;

@Component
@Slf4j
public class MetricBuilder {
    // TODO: Metrics to add
    //  Assertions, Total-Run-Time? (Last USER timestamp - RUN timestamp),
    //  Request-Aggregations (oder geht das mit Influx?)

    private static final String REQUEST_DURATION = "request_duration";
    private static final String REQUEST_COUNT = "request_count";
    private static final String REQUEST_FAILURE_RATE = "request_failure_rate";
    private static final String USER_COUNT = "user_count";

    public List<MetricData> buildMetrics(List<String[]> rawMetrics) {
        Map<String, List<LongPointData>> pointData = new HashMap<>();
        pointData.put(REQUEST_DURATION, new LinkedList<>());
        pointData.put(REQUEST_COUNT, new LinkedList<>());
        pointData.put(REQUEST_FAILURE_RATE, new LinkedList<>());
        pointData.put(USER_COUNT, new LinkedList<>());

        AtomicLong requestCounter = new AtomicLong(0L);
        AtomicLong requestFailureCounter = new AtomicLong(0L);
        AtomicLong userCounter = new AtomicLong(0L);

        rawMetrics.forEach(line -> {
            String type = line[0];

            if(type.equals(RUN.name())) {
               SimulationData simulationData = new SimulationData(line);
               LongPointData point = simulationData.createPointData();
               //pointData.get(RUN).add(point);
            }
            else if(type.equals(USER.name())) {
                UserData userData = new UserData(line);
                if(userData.getType().equals("START")) userCounter.getAndIncrement();
                else userCounter.getAndDecrement();

                LongPointData point = userData.createPointData();
                LongPointData countPoint = userData.createCounterData(userCounter.get());
                pointData.get(USER_COUNT).add(countPoint);
            }
            else if(type.equals(REQUEST.name())) {
                RequestData requestData = new RequestData(line);
                requestCounter.getAndIncrement();
                if (!requestData.getStatus().equals("OK")) requestFailureCounter.getAndIncrement();

                LongPointData durationPoint = requestData.createPointData();
                LongPointData countPoint = requestData.createCounterData(requestCounter.get());
                pointData.get(REQUEST_DURATION).add(durationPoint);
                pointData.get(REQUEST_COUNT).add(countPoint);
            }
            else throw new UnknownTypeException(type);
        });

        LongPointData failureData = createRequestFailureData(requestFailureCounter.get(), requestCounter.get());
        pointData.get(REQUEST_FAILURE_RATE).add(failureData);

        return createMetrics(pointData);
    }

    private static MetricData createMetric(GatlingSimulationType type, LongPointData data) {
        return ImmutableMetricData.createLongGauge(
                Resource.getDefault(),
                InstrumentationScopeInfo.empty(),
                type.name(),
                "Gatling Long Metric",
                "ms",
                ImmutableGaugeData.create(Collections.singletonList(data))
        );
    }

    private LongPointData createRequestFailureData(Long requestFailureCounter, Long requestCounter) {
        Long timestamp = TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis());
        Attributes attributes = Attributes.empty();

        Long failureRate = (requestFailureCounter/requestCounter) * 100L;

        return ImmutableLongPointData.create(
                timestamp,
                timestamp,
                attributes,
                failureRate
        );
    }

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
     * Determines the unit based on the metric name
     * @param name metric name
     * @return unit for the provided metric
     */
    private String getUnit(String name) {
        if (name.endsWith("rate")) return "%";
        else if (name.endsWith("duration")) return "ms";
        else return "";
    }
}
