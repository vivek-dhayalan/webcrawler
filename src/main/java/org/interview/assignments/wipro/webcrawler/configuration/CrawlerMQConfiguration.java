package org.interview.assignments.wipro.webcrawler.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Configuration
public class CrawlerMQConfiguration {

    private static final String CRAWLER_REQUEST_QUEUE = "assignment.webcrawler.request.queue";

    private static final String CRAWLER_EXCHANGE = "assignment.webcrawler";

    private static final String CRAWLER_DLX = "assignment.webcrawler.dlx";
    private static final String CRAWLER_DLQ = "assignment.webcrawler.request.dlq";

    private static final int NUMBER_OF_CONCURRENT_CONSUMERS = 20;

    public static final int MAX_PRIORITY = 10;

    @Bean
    public DirectExchange crawlerExchange() {

        return new DirectExchange(CRAWLER_EXCHANGE);
    }

    @Bean
    public SimpleMessageListenerContainer crawlRequestMessageListenerContainer(ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        messageListenerContainer.setQueues(crawlerRequestQueue());
        messageListenerContainer.setConcurrentConsumers(NUMBER_OF_CONCURRENT_CONSUMERS);
        messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        messageListenerContainer.setAutoStartup(false);

        return messageListenerContainer;
    }

    @Bean
    public Queue crawlerRequestQueue() {

        Map<String, Object> queueArgs = createQueueArgs();
        queueArgs.put("x-dead-letter-exchange", CRAWLER_DLX);
        queueArgs.put("x-dead-letter-routing-key", CRAWLER_DLQ);
        queueArgs.put("x-max-priority", MAX_PRIORITY);

        return new Queue(CRAWLER_REQUEST_QUEUE, true, false, false, queueArgs);
    }

    @Bean
    public Binding bindingCrawlerRequestQueue() {

        return BindingBuilder.bind(crawlerRequestQueue()).to(crawlerExchange()).withQueueName();
    }

    @Bean(name = "rabbitTemplate")
    @Primary
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setExchange(CRAWLER_REQUEST_QUEUE);
        return rabbitTemplate;
    }

    private Map<String, Object> createQueueArgs() {

        Map<String, Object> args = new HashMap<>();
        args.put("x-ha-policy", "all");
        return args;
    }

}
