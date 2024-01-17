package poc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import poc.dqlang.loadtest.LoadTestConfiguration;

import static poc.config.MQConfig.LOAD_TEST_QUEUE;

@Component
@Slf4j
public class ConfigurationReceiver {

    @Autowired
    private AdaptationManager adaptationManager;

    /**
     * Import the loadtest configuration and start the adaptation process
     * @param loadTestConfig Imported loadtest configuration
     */
    //@RabbitListener(queues = LOAD_TEST_QUEUE)
    public void receive(@Payload LoadTestConfiguration loadTestConfig) {
        // TODO Uncomment
        //adaptationManager.start(loadTestConfig);
    }
}
