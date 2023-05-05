package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.GameId;
import ch.zuehlke.common.Move;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.fullstack.hackathon.controller.JoinResult;
import ch.zuehlke.fullstack.hackathon.controller.JoinResult.JoinResultType;
import ch.zuehlke.fullstack.hackathon.controller.PlayResult;
import ch.zuehlke.fullstack.hackathon.controller.StartResult;
import ch.zuehlke.fullstack.hackathon.model.Game;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    public boolean deleteGame(GameId gameId) {
        return games.removeIf(game -> game.getGameId().equals(gameId));
    }

    public Optional<Game> getGame(GameId gameId) {
        return games.stream()
                .filter(game -> game.getGameId().equals(gameId))
                .findFirst();
    }

    public JoinResult join(GameId gameId, PlayerName name) {
        Optional<Game> game = getGame(gameId);
        if (game.isEmpty()) {
            return new JoinResult(null, JoinResultType.GAME_NOT_FOUND);
        }

        boolean success = game.get().addPlayer(name);
        if (!success) {
            return new JoinResult(null, JoinResultType.GAME_FULL);
        }

        return new JoinResult(name, JoinResultType.SUCCESS);
    }

    public StartResult startGame(GameId gameId) {
        Optional<Game> optionalGame = getGame(gameId);
        if (optionalGame.isEmpty()) {
            return StartResult.NOT_FOUND;
        }

        Game game = optionalGame.get();
        if (!game.canStartGame()) {
            return StartResult.NOT_ENOUGH_PLAYERS;
        }

        game.startGame();

        return StartResult.SUCCESS;
    }

    public PlayResult play(Move move, GameId gameId) {
        Optional<Game> optionalGame = getGame(gameId);
        if (optionalGame.isEmpty()) {
            return PlayResult.GAME_NOT_FOUND;
        }

        Game game = optionalGame.get();
        if (!game.isMoveAllowed(move)) {
            return PlayResult.INVALID_ACTION;
        }

        game.playMove(move);

        return PlayResult.SUCCESS;
    }

    public List<Game> getGames(List<GameId> gameIds) {
        List<Game> games = new ArrayList<>();
        for (GameId gameId : gameIds) {
            Optional<Game> game = getGame(gameId);
            game.ifPresent(games::add);
        }
        return games;
    }
}
