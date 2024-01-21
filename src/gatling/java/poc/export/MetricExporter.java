package poc.export;

import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter;
import io.opentelemetry.sdk.metrics.data.MetricData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static poc.util.CustomLogger.printError;
import static poc.util.CustomLogger.printLog;

@Component
public class MetricExporter {
    private OtlpHttpMetricExporter exporter;

    @Autowired
    private CSVImporter csvImporter;
    @Autowired
    private MetricBuilder metricBuilder;

    public void export() {
        printLog(this.getClass(), "EXPORTING METRICS");
        this.buildExporter();

        List<String[]> records;
        try {
            records = csvImporter.importMetrics();
        } catch (IOException e) {
            printError(this.getClass(), "READING DATA FROM CSV FAILED");
            throw new RuntimeException(e);
        }
        printLog(this.getClass(), "RAW METRICS HAVE BEEN READ FROM CSV");

        List<MetricData> metricData = metricBuilder.buildMetrics(records);
        printLog(this.getClass(), "METRICS HAVE BEEN CREATED FOR EXPORTER");

        exporter.export(metricData);
        printLog(this.getClass(), "METRICS HAVE BEEN EXPORTED");
    }

    private void buildExporter() {
        String otelHost = getCollectorHost();
        exporter = OtlpHttpMetricExporter.builder()
                .setEndpoint("http://" + otelHost + ":4318/v1/metrics")
                .setTimeout(Duration.ofSeconds(1))
                .build();
        printLog(this.getClass(), "USING EXPORTER: " + exporter);
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
