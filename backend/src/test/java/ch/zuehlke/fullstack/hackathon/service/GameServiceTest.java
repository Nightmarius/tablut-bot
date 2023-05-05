package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.*;
import ch.zuehlke.fullstack.hackathon.controller.JoinResult;
import ch.zuehlke.fullstack.hackathon.controller.PlayResult;
import ch.zuehlke.fullstack.hackathon.controller.StartResult;
import ch.zuehlke.fullstack.hackathon.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameServiceTest {

    private GameService gameService;
    private GameId fakeGameId = new GameId(666);

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    void getGames_returnsEmptyList() {
        List<Game> games = gameService.getGames();

        assertThat(games).hasSize(0);
    }

    @Test
    void createGame_addsNewGameToList() {
        Game createdGame = gameService.createGame();

        assertThat(createdGame.getGameId()).isEqualTo(new GameId(1));
        assertThat(gameService.getGames()).hasSize(1);
    }

    @Test
    void deleteGame_successfully() {
        Game game = gameService.createGame();
        assertThat(gameService.getGames()).hasSize(1);

        boolean success = gameService.deleteGame(game.getGameId());

        assertThat(gameService.getGames()).hasSize(0);
        assertThat(success).isTrue();
    }

    @Test
    void deleteGame_nonExistingGame_returnsFalse() {
        gameService.createGame();
        assertThat(gameService.getGames()).hasSize(1);

        boolean success = gameService.deleteGame(fakeGameId);

        assertThat(gameService.getGames()).hasSize(1);
        assertThat(success).isFalse();
    }

    @Test
    void joinGame_successfully() {
        Game game = gameService.createGame();

        JoinResult joinResult = gameService.join(game.getGameId(), new PlayerName("playerName"));

        assertThat(joinResult.resultType()).isEqualTo(JoinResult.JoinResultType.SUCCESS);
        assertThat(joinResult.name()).isNotNull();
    }

    @Test
    void joinGame_threeTimes_gameIsFull() {
        Game game = gameService.createGame();

        gameService.join(game.getGameId(), new PlayerName("name1"));
        gameService.join(game.getGameId(), new PlayerName("name2"));
        JoinResult joinResult = gameService.join(game.getGameId(), new PlayerName("name3"));

        assertThat(joinResult.resultType()).isEqualTo(JoinResult.JoinResultType.GAME_FULL);
        assertThat(joinResult.name()).isNull();
    }

    @Test
    void joinGame_nonExistingGame_gameIsNotFound() {
        JoinResult joinResult = gameService.join(fakeGameId, new PlayerName("playerName"));

        assertThat(joinResult.resultType()).isEqualTo(JoinResult.JoinResultType.GAME_NOT_FOUND);
        assertThat(joinResult.name()).isNull();
    }

    @Test
    void startGame_withZeroPlayers_notEnoughPlayers() {
        Game game = gameService.createGame();

        StartResult startResult = gameService.startGame(game.getGameId());

        assertThat(startResult.resultType()).isEqualTo(StartResult.StartResultType.NOT_ENOUGH_PLAYERS);
        assertThat(game.getStatus()).isEqualTo(GameStatus.NOT_STARTED);
    }

    @Test
    void startGame_nonExistingGame_gameIsNotFound() {
        StartResult startResult = gameService.startGame(fakeGameId);

        assertThat(startResult.resultType()).isEqualTo(StartResult.StartResultType.GAME_NOT_FOUND);
    }

    @Test
    void startGame_gameIsFull_successfully() {
        Game game = gameService.createGame();
        gameService.join(game.getGameId(), new PlayerName("name1"));
        gameService.join(game.getGameId(), new PlayerName("name2"));


        StartResult startResult = gameService.startGame(game.getGameId());

        assertThat(startResult.resultType()).isEqualTo(StartResult.StartResultType.SUCCESS);
        assertThat(game.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    void playGame_playerOnePlays_successfully() {
        Game game = gameService.createGame();
        JoinResult joinResult1 = gameService.join(game.getGameId(), new PlayerName("name1"));
        gameService.join(game.getGameId(), new PlayerName("name2"));
        PlayerName playerId1 = joinResult1.name();
        gameService.startGame(game.getGameId());
        RequestId requestIdForPlayer1 = getRequestIdForPlayer(playerId1, game);

        Move move = new Move(playerId1, requestIdForPlayer1, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        PlayResult playResult = gameService.play(move, game.getGameId());

        assertThat(playResult).isEqualTo(PlayResult.SUCCESS);
    }

    @Test
    void playGame_playerOnePlaysTwice_returnsInvalidAction() {
        Game game = gameService.createGame();
        JoinResult joinResult1 = gameService.join(game.getGameId(), new PlayerName("name1"));
        gameService.join(game.getGameId(), new PlayerName("name2"));
        PlayerName playerId1 = joinResult1.name();
        gameService.startGame(game.getGameId());
        RequestId requestIdForPlayer1 = getRequestIdForPlayer(playerId1, game);

        Move move = new Move(playerId1, requestIdForPlayer1, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        gameService.play(move, game.getGameId());
        PlayResult playResult = gameService.play(move, game.getGameId());

        assertThat(playResult).isEqualTo(PlayResult.INVALID_ACTION);
    }

    @Test
    void playGame_withNonExistingGameId_returnsGameNotFound() {
        Game game = gameService.createGame();
        JoinResult joinResult1 = gameService.join(game.getGameId(), new PlayerName("name1"));
        gameService.join(game.getGameId(), new PlayerName("name2"));
        PlayerName playerId1 = joinResult1.name();
        gameService.startGame(game.getGameId());
        RequestId requestIdForPlayer1 = getRequestIdForPlayer(playerId1, game);

        Move move = new Move(playerId1, requestIdForPlayer1, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        PlayResult playResult = gameService.play(move, new GameId(666));

        assertThat(playResult).isEqualTo(PlayResult.GAME_NOT_FOUND);
    }

    @Test
    void playGame_withInvalidRequestId_returnsInvalidAction() {
        Game game = gameService.createGame();
        JoinResult joinResult1 = gameService.join(game.getGameId(), new PlayerName("name1"));
        gameService.join(game.getGameId(), new PlayerName("name2"));
        PlayerName playerId1 = joinResult1.name();
        gameService.startGame(game.getGameId());

        Move move = new Move(playerId1, new RequestId(), game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        PlayResult playResult = gameService.play(move, game.getGameId());

        assertThat(playResult).isEqualTo(PlayResult.INVALID_ACTION);
    }

    private RequestId getRequestIdForPlayer(PlayerName playerId, Game game) {
        return game.getState().currentRequests().stream()
                .filter(request -> request.playerName().equals(playerId))
                .findFirst()
                .map(PlayRequest::requestId)
                .orElse(new RequestId());
    }
}
