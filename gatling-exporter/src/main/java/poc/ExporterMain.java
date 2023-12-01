package poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ExporterMain {
    public static void main(String[] args) {
        log.info("STARTING GATLING EXPORTER");
        SpringApplication.run(ExporterMain.class, args);
    }
}
