package poc.export;

import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableGaugeData;
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
import java.util.logging.Logger;

import static poc.export.data.GatlingSimulationType.*;

@Component
@Slf4j
public class MetricBuilder {

    public List<MetricData> buildMetrics(List<String[]> rawMetrics) {
        Map<GatlingSimulationType, List<LongPointData>> pointData = new HashMap<>();
        //pointData.put(RUN, new LinkedList<>());
        //pointData.put(USER, new LinkedList<>());
        pointData.put(REQUEST, new LinkedList<>());
        List<MetricData> metricData = new LinkedList<>();

        rawMetrics.forEach(line -> {
           String type = line[0];
           if(type.equals(RUN.name())) {
               SimulationData simulationData = new SimulationData(line);
               LongPointData point = simulationData.createPointData();
               //pointData.get(RUN).add(point);
           }
           else if(type.equals(USER.name())) {
               UserData userData = new UserData(line);
               LongPointData point = userData.createPointData();
               //pointData.get(USER).add(point);
            }
           else if(type.equals(REQUEST.name())) {
               RequestData requestData = new RequestData(line);
               LongPointData point = requestData.createPointData();
               log.info(point.toString());
               //pointData.get(REQUEST).add(point);
               metricData.add(createMetric(REQUEST, point));
            }
           else throw new UnknownTypeException(type);
        });

        //return createMetrics(pointData);
        return metricData;
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

    private static List<MetricData> createMetrics(Map<GatlingSimulationType, List<LongPointData>> pointData) {
        List<MetricData> metricData = new LinkedList<>();

        pointData.forEach((type, data) -> {
            MetricData metric = ImmutableMetricData.createLongGauge(
                    Resource.getDefault(),
                    InstrumentationScopeInfo.create("github.io.dqualizer.gatling"),
                    type.name(),
                    "Gatling Long Metric",
                    "ms",
                    ImmutableGaugeData.create(data)
            );
            metricData.add(metric);
        });

        return metricData;
    }
}
