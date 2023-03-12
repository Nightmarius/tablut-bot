package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.GameId;
import ch.zuehlke.common.Player;
import ch.zuehlke.common.PlayerId;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.fullstack.hackathon.controller.JoinResult;
import ch.zuehlke.fullstack.hackathon.controller.JoinResult.JoinResultType;
import ch.zuehlke.fullstack.hackathon.model.Game;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameService {

    // Improve: Instead of storing this in-memory, store it in a database
    private final List<Game> games = new ArrayList<>();
    private static int counter = 0;

    public List<Game> getGames() {
        return games;
    }

    public Game createGame() {
        // Improve: Find a better way to create game ids
        counter += 1;
        Game game = new Game(new GameId(counter));
        games.add(game);
        return game;
    }


    public boolean deleteGame(int gameId) {
        return games.removeIf(game -> game.getGameId().value() == gameId);
    }

    public Optional<Game> getGame(int gameId) {
        return games.stream()
                .filter(game -> game.getGameId().value() == gameId)
                .findFirst();
    }

    public JoinResult join(int gameId, PlayerName name) {
        Optional<Game> game = getGame(gameId);
        if (game.isEmpty()) {
            return new JoinResult(null, JoinResultType.GAME_NOT_FOUND);
        }
        Player newPlayer = new Player(new PlayerId(UUID.randomUUID().toString()), name);

        boolean success = game.get().addPlayer(newPlayer);
        if (!success) {
            return new JoinResult(null, JoinResultType.GAME_FULL);
        }

        return new JoinResult(newPlayer.id(), JoinResultType.SUCCESS);
    }
}
