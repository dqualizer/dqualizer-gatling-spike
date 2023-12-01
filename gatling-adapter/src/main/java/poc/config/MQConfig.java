package poc.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public final static String LOAD_TEST_QUEUE = "loadtest";
    public final static String LOAD_TEST_EXCHANGE = "translator-loadtest";
    public final static String GET_KEY = "get";


    @Bean
    public TopicExchange loadtestExchange() { return new TopicExchange(LOAD_TEST_EXCHANGE); }

    @Bean
    public Queue loadtestQueue() { return new Queue(LOAD_TEST_QUEUE, false); }

    @Bean
    public Binding loadtestBinding(@Qualifier("loadtestQueue") Queue queue,
                                   @Qualifier("loadtestExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(GET_KEY);
    }

    @Bean
    public MessageConverter messageConverter() { return new Jackson2JsonMessageConverter(); }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
