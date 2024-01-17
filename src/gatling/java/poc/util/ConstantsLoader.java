package poc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import poc.config.FileConfig;
import poc.dqlang.constants.LoadTestConstants;
import poc.exception.InvalidSchemaException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConstantsLoader {

    public static LoadTestConstants load() {
        String filePath = FileConfig.getConstantsPath();
        String constantsString;
        try {
            constantsString = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        LoadTestConstants loadTestConstants;

        try {
            loadTestConstants = objectMapper.readValue(constantsString, LoadTestConstants.class);
        } catch (Exception e) {
            throw new InvalidSchemaException(e.getMessage());
        }

        return loadTestConstants;
    }
}
