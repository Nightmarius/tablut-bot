package ch.zuehlke.challenge.bot.client;

import ch.zuehlke.challenge.bot.service.ShutDownService;
import ch.zuehlke.challenge.bot.util.ApplicationProperties;
import ch.zuehlke.common.JoinRequest;
import ch.zuehlke.common.JoinResponse;
import ch.zuehlke.common.Move;
import ch.zuehlke.common.PlayerName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameClient {

    private final RestTemplate restTemplateClient;

    private final ApplicationProperties applicationProperties;

    private final ShutDownService shutDownService;

    public PlayerName join() {
        JoinRequest signUpRequest = new JoinRequest(new PlayerName(applicationProperties.getName()));
        log.info("Joining lobby with request {}", signUpRequest);

        try {
            ResponseEntity<JoinResponse> signUpResponse = restTemplateClient
                    .postForEntity(applicationProperties.getBackendJoinUrl(),
                            new HttpEntity<>(signUpRequest),
                            JoinResponse.class,
                            1
                    );

            PlayerName name = Objects.requireNonNull(signUpResponse.getBody()).name();
            log.info("Joined lobby with PlayerName: {}", name);
            return name;
        } catch (RestClientException | NullPointerException e) {
            log.error("Could not join lobby. Will shutdown now...\nError: {}", e.getMessage());
            shutDownService.shutDown();
            return null;
        }
    }

    public void play(Move move) {
        log.info("Playing move: {}", move);

        try {
            ResponseEntity<Void> response = restTemplateClient
                    .postForEntity(applicationProperties.getBackendPlayUrl(),
                            new HttpEntity<>(move),
                            Void.class,
                            move.gameId().value()
                    );

            log.info("Successfully played a move!");
        } catch (RestClientException e) {
            log.error("Could not play move. Will shutdown now...\nError: {}", e.getMessage());
            shutDownService.shutDown();
        }
    }
}
