package rs.qubit.workprocessor;

import lombok.SneakyThrows;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundGateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@IntegrationComponentScan
class IntegrationConfig {
    public static final String QUEUE_NAME = "example-queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public MessageChannel amqpInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public AmqpInboundGateway inbound(SimpleMessageListenerContainer listenerContainer,
                                      @Qualifier("amqpInputChannel") MessageChannel channel) {
        AmqpInboundGateway gateway = new AmqpInboundGateway(listenerContainer);
        gateway.setRequestChannel(channel);
        return gateway;
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setConcurrentConsumers(2);

        return container;
    }

    @Bean
    @ServiceActivator(inputChannel = "amqpInputChannel")
    public MessageHandler handler() {
        return new AbstractReplyProducingMessageHandler() {

            @SneakyThrows
            @Override
            protected Object handleRequestMessage(Message<?> requestMessage) {
                Thread.sleep(10000L);
                return "reply to " + requestMessage.getPayload();
            }
        };
    }
}
