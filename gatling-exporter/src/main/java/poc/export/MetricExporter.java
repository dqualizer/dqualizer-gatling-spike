package poc.export;

import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter;
import io.opentelemetry.sdk.metrics.data.MetricData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class MetricExporter {
    private OtlpHttpMetricExporter exporter;

    @Autowired
    private CSVImporter csvImporter;
    @Autowired
    private MetricBuilder metricBuilder;

    @EventListener(ApplicationReadyEvent.class)
    public void export() throws IOException {
        this.buildExporter();

        List<String[]> rawMetrics = csvImporter.importMetrics();
        log.info("RAW METRICS HAVE BEEN READ FROM CSV");

        List<MetricData> metricData = metricBuilder.buildMetrics(rawMetrics);
        log.info("METRICS HAVE BEEN CREATED FOR EXPORTER");

        exporter.export(metricData);
        log.info("METRICS HAVE BEEN EXPORTED");
    }

    private void buildExporter() {
        String otelHost = getCollectorHost();
        exporter = OtlpHttpMetricExporter.builder()
                .setEndpoint("http://" + otelHost + ":4318/v1/metrics")
                .setTimeout(Duration.ofSeconds(1))
                .build();
        log.info("USING EXPORTER: " + exporter);
    }

    /**
     * @return Host on which a OpenTelemetry Collector is running
     */
    private String getCollectorHost() {
        String host = System.getenv("OTEL_HOST");
        if (host != null) return host;
        else return "localhost";
    }
}
