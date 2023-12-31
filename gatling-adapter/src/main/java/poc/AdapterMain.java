package poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AdapterMain {
    public static void main(String[] args) {
        log.info("STARTING GATLING ADAPTER");
        SpringApplication.run(AdapterMain.class, args);
    }
}
