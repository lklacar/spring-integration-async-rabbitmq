package rs.qubit.workdispatcher;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
class WorkController {
    private final MyGateway workGateway;

    public WorkController(MyGateway workGateway) {
        this.workGateway = workGateway;
    }

    @GetMapping("/")
    public Mono<String> startWork() {
        return workGateway.sendToRabbit("Example data");
    }
}
