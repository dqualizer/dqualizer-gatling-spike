package poc.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Since Gatling disables the logging framework after first test execution,
 * this custom logging class is necessary
 * <br>
 * This class uses the System.out.println() instead, since it is not disabled by Gatling.
 * Currently, different log levels are not possible
 */
public class CustomLogger {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static void printLog(Class<?> object, String message) {
        String log = createLog(object, message);
        System.out.println(log);
    }

    public static void printError(Class <?> object, String message) {
        String log = createLog(object, message);
        System.err.println(log);
    }

    private static String createLog(Class<?> object, String message) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String formattedTimestamp = dateFormat.format(timestamp);

        String threadName = Thread.currentThread().getName();
        long processId = ProcessHandle.current().pid();

        return formattedTimestamp + "  " +
                "CUSTOM" + " " +
                processId + " --- " +
                "[" + threadName + "] " +
                object.getCanonicalName() + "\t\t: " +
                message;
    }
}
