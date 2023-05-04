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
import org.springframework.web.client.RestTemplate;

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

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", applicationProperties.getToken());
        HttpEntity<JoinRequest> httpEntity = new HttpEntity<>(signUpRequest, headers);

        // Improve: Handle exceptions
        ResponseEntity<JoinResponse> signUpResponse = restTemplateClient
                .postForEntity(applicationProperties.getBackendJoinUrl(),
                        httpEntity,
                        JoinResponse.class
                );
        log.info("Received response: {}", signUpResponse);
        if (signUpResponse.getStatusCode().is2xxSuccessful() && signUpResponse.getBody() != null) {
            PlayerName name = signUpResponse.getBody().name();
            log.info("Joined lobby with PlayerName: {}", name);
            return name;
        } else {
            log.error("Could not join lobby. Will shutdown now...");
            shutDownService.shutDown();
            // Needed to return something even though exit(0) is called
            return null;
        }
    }

    public void play(Move move) {
        log.info("Playing move: {}", move);

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", applicationProperties.getToken());
        HttpEntity<Move> httpEntity = new HttpEntity<>(move, headers);

        log.info("Sending play request with entity: {}", httpEntity);

        // Improve: Handle exceptions
        ResponseEntity<Void> response = restTemplateClient
                .postForEntity(applicationProperties.getBackendPlayUrl(),
                        httpEntity,
                        Void.class,
                        move.gameId().value()
                );
        log.info("Received response: {}", response);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Successfully played a move!");
        } else {
            log.error("Could not play game! Will shutdown now...");
            shutDownService.shutDown();
        }
    }
}
