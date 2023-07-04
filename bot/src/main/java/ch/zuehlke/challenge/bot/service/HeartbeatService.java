package ch.zuehlke.challenge.bot.service;

import ch.zuehlke.challenge.bot.util.ApplicationProperties;
import ch.zuehlke.common.HeartbeatRequest;
import ch.zuehlke.common.PlayerName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class HeartbeatService {

    private final RestTemplate restTemplateClient;

    private final ApplicationProperties applicationProperties;

    private final GameService gameService;

    private final ShutDownService shutDownService;

    @Async
    @Scheduled(fixedRate = 1000) // every second
    public void sendHeartbeat() {
        if (gameService.getPlayerName() == null) {
            log.info("PlayerName is null. Waiting for bot to join lobby...");
            return;
        }

        HeartbeatRequest heartbeatRequest = new HeartbeatRequest(new PlayerName(applicationProperties.getName()));
        log.debug("Sending Heartbeat with request {}", heartbeatRequest);

        try {
            ResponseEntity<Void> heartbeatResponse = restTemplateClient
                    .postForEntity(applicationProperties.getBackendHeartbeatUrl(),
                            new HttpEntity<>(heartbeatRequest),
                            Void.class
                    );

            log.debug("Heartbeat response code: {}", heartbeatResponse.getStatusCode());
        } catch (RestClientException e) {
            log.error("Could not send Heartbeat. Will shutdown now...\nError: {}", e.getMessage());
            shutDownService.shutDown();
        }
    }
}
