package rs.qubit.workdispatcher;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import reactor.core.publisher.Mono;

@MessagingGateway(defaultRequestChannel = "amqpOutboundChannel", defaultReplyTimeout = "100000")
interface MyGateway {

    @Gateway(replyTimeout = 100000L)
    Mono<String> sendToRabbit(String data);
}
