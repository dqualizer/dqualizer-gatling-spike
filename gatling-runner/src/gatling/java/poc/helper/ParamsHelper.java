package poc.helper;

import com.typesafe.config.Config;
import io.gatling.javaapi.core.FeederBuilder;
import poc.exception.UnknownTypeException;

import java.util.logging.Logger;

import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.core.CoreDsl.jsonFile;

public class ParamsHelper {

    private final Logger logger = Logger.getLogger(RequestHelper.class.getName());
    private final Config params;

    public ParamsHelper(Config params) {
        this.params = params;
    }

    // Currently, only json & csv are supported
    public FeederBuilder<?> getFeederBuilder(String feeder) {
        String feederPath = params.getString(feeder);
        logger.info("Using FEEDER: " + feederPath);

        if(feederPath.endsWith(".json")) return jsonFile(feederPath);
        else if(feederPath.endsWith(".json.zip")) return jsonFile(feederPath).unzip();
        else if(feederPath.endsWith(".csv")) return csv(feederPath);
        else if(feederPath.endsWith(".csv.zip")) return csv(feederPath).unzip();
        else throw new UnknownTypeException(feederPath);
    }
}
