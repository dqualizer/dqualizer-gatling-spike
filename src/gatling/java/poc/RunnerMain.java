package poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import poc.service.AdaptationManager;

@SpringBootApplication
@Slf4j
public class RunnerMain {

    public static void main(String[] args) {
        log.info("STARTING GATLING ADAPTER");
        SpringApplication.run(RunnerMain.class, args);
    }
}
