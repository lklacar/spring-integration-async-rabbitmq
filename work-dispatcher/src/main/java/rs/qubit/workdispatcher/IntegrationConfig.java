package rs.qubit.workdispatcher;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

@Configuration
@IntegrationComponentScan
class IntegrationConfig {
    public static final String QUEUE_NAME = "example-queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setReplyTimeout(1000000L);
        return rabbitTemplate;
    }

    @Bean
    public IntegrationFlow amqpOutbound(AmqpTemplate amqpTemplate) {
        return IntegrationFlows.from(amqpOutboundChannel())
                .handle(Amqp.outboundGateway(amqpTemplate)
                        .routingKey(QUEUE_NAME))
                .get();
    }

    @Bean
    public MessageChannel amqpOutboundChannel() {
        return new DirectChannel();
    }
}
