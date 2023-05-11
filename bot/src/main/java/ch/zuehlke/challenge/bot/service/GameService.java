package ch.zuehlke.challenge.bot.service;

import ch.zuehlke.challenge.bot.brain.Bot;
import ch.zuehlke.challenge.bot.client.GameClient;
import ch.zuehlke.common.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final Bot bot;

    @Getter
    @Setter
    private PlayerName playerName;

    private final GameClient gameClient;

    // Improve: find a better way to keep track of already processed requests
    private final Set<RequestId> alreadyProcessedRequestIds = new HashSet<>();

    @EventListener(ApplicationReadyEvent.class)
    public void join() {
        this.playerName = gameClient.join();
    }

    public void onGameUpdate(GameDto gameDto) {
        // Improve: use this to get updates from the bots
        if (gameDto.status() == GameStatus.NOT_STARTED) {
            log.info("Not taking any action, game is not started yet...");
            return;
        }

        if (gameDto.state().playRequest() != null
                && gameDto.state().playRequest().playerName().equals(playerName)
                && !alreadyProcessedRequestIds.contains(gameDto.state().playRequest().requestId()))
            processRequest(gameDto.state().playRequest());
    }

    private void processRequest(PlayRequest playRequest) {
        log.info("Processing request: {}", playRequest);
        alreadyProcessedRequestIds.add(playRequest.requestId());

        GameAction decision = bot.decide(playRequest.attacker(), playRequest.board(), playRequest.possibleActions());

        Move move = new Move(playerName, playRequest.requestId(), playRequest.gameId(), decision);
        gameClient.play(move);
    }


}
