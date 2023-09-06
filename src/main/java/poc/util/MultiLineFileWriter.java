package poc.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class MultiLineFileWriter {

    private static final Logger logger = Logger.getLogger(MultiLineFileWriter.class.getName());

    public static void write(List<String> lines, String filePath) throws IOException {
        if(lines == null) {
            logger.warning("WRITING FAILED - EMPTY STRING");
            return;
        }
        File file = new File(filePath);
        if(!file.exists()) file.createNewFile();
        FileWriter writer = new FileWriter(file);

        for(String line: lines) writer.write(line);
        writer.close();
        logger.info("WRITING WAS SUCCESSFUL");
    }
}
