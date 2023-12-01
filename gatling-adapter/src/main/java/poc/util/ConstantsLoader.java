package poc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.config.FileConfig;
import poc.dqlang.constants.GatlingConstants;
import poc.exception.InvalidSchemaException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ConstantsLoader {

    @Autowired
    private FileConfig fileConfig;

    public GatlingConstants load() {
        String filePath = fileConfig.getConstantsPath();
        String constantsString = "";
        try {
            constantsString = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        GatlingConstants gatlingConstants;

        try {
            gatlingConstants = objectMapper.readValue(constantsString, GatlingConstants.class);
        } catch (Exception e) {
            throw new InvalidSchemaException(e.getMessage());
        }

        return gatlingConstants;
    }
}
