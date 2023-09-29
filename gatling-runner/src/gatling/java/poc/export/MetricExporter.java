package poc.export;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MetricExporter {

    public static void export() throws IOException {
        List<String[]> rawMetrics = CSVImporter.importMetrics();
    }
}
