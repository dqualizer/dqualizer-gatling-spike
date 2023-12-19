package poc.export;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.config.FileConfig;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Util class, to import data from the last created simulation.log
 * The simulation.log can be read as a csv file, which uses '\t' as separator
 */
@Component
@Slf4j
public class CSVImporter {

    @Autowired
    private FileConfig fileConfig;

    public List<String[]> importMetrics() throws IOException {
        String simulationLogPath = getSimulationLogPath();
        log.info("READING DATA FROM CSV: " + simulationLogPath);

        return readCSV(simulationLogPath);
    }

    private String getSimulationLogPath() {
        List<Path> simulationFolders = new LinkedList<>();

        // Read all existing sub-folders of the result folder
        Path resultsFolderPath = fileConfig.getResultFilePath();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resultsFolderPath)) {
            for(Path path : directoryStream) simulationFolders.add(path);
        } catch (IOException e) {
            throw new RuntimeException("ERROR IN READING THE RESULTS FOLDER", e);
        }

        // Sort all simulation-folder by their modified time
        simulationFolders.sort(Comparator.comparingLong((Path path) -> {
            try {
                return Files.getLastModifiedTime(path).toMillis();
            } catch (IOException e) {
                throw new RuntimeException("ERROR IN SORTING THE SIMULATION FOLDERS", e);
            }
        }).reversed());

        if(!simulationFolders.isEmpty()) {
            String lastCreatedSimulationFolder = simulationFolders.get(0).toString();
            String lastSimulationLog = lastCreatedSimulationFolder + "/simulation.log";
            return lastSimulationLog;
        }
        else throw new RuntimeException("NO SIMULATION FOLDERS FOUND");
    }

    private List<String []> readCSV(String filePath) throws IOException {
        InputStream stream = new FileInputStream(filePath);
        Reader streamReader = new InputStreamReader(stream);

        CSVParser parser = new CSVParserBuilder()
                .withSeparator('\t')
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        CSVReader reader = new CSVReaderBuilder(streamReader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();

        List<String[]> csv = reader.readAll();
        reader.close();

        return csv;
    }
}
