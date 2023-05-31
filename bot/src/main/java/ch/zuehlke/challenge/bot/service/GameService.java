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

    private final Set<RequestId> alreadyProcessedRequestIds = new HashSet<>();

    @EventListener(ApplicationReadyEvent.class)
    public void join() {
        this.playerName = gameClient.join();
    }

    public void onGameUpdate(GameDto gameDto) {
        if (isRelevantUpdate(gameDto)) {
            processRequest(gameDto.state().playRequest());
        }
    }

    private boolean isRelevantUpdate(GameDto gameDto) {
        return gameDto.status() == GameStatus.IN_PROGRESS &&
                gameDto.state().playRequest() != null
                && gameDto.state().playRequest().playerName().equals(playerName)
                && !alreadyProcessedRequestIds.contains(gameDto.state().playRequest().requestId());
    }

    private void processRequest(PlayRequest playRequest) {
        log.info("Processing request: {}", playRequest);
        alreadyProcessedRequestIds.add(playRequest.requestId());

        GameAction decision = bot.decide(playRequest.attacker(), playRequest.board(), playRequest.possibleActions());

        Move move = new Move(playerName, playRequest.requestId(), playRequest.gameId(), decision);
        gameClient.play(move);
    }


}
