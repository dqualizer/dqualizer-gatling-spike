package poc.adapter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class MultiLineFileWriter {

    private final Logger logger = Logger.getLogger(MultiLineFileWriter.class.getName());

    public void write(List<String> lines, String filePath) throws IOException {
        if(lines == null) {
            logger.warning("WRITING FAILED - EMPTY STRING");
            return;
        }
        FileWriter writer = new FileWriter(filePath);

        for(String line: lines) writer.write(line);
        writer.close();
        logger.info("WRITING WAS SUCCESSFUL");
    }
}
