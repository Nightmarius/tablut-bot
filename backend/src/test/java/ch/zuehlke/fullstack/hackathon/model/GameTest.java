package ch.zuehlke.fullstack.hackathon.model;

import ch.zuehlke.common.*;
import ch.zuehlke.common.Coordinates;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    void addPlayer_successfully() {
        Player player = new Player(new PlayerId(), new PlayerName("name"));
        Game game = new Game(new GameId(1));

        game.addPlayer(player);

        assertThat(game.getPlayers()).hasSize(1);
        assertThat(game.getPlayers().get(0)).isEqualTo(player);
    }

    @Test
    void startGame_withTwoPlayers_successfully() {
        Game game = new Game(new GameId(1));
        PlayerId playerId1 = new PlayerId();
        game.addPlayer(new Player(playerId1, new PlayerName("name1")));
        PlayerId playerId2 = new PlayerId();
        game.addPlayer(new Player(playerId2, new PlayerName("name2")));

        game.startGame();

        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertThat(game.getState().currentRequests()).hasSize(1);
        assertThat(game.getState().currentRequests().stream().anyMatch(request -> request.playerId().equals(playerId1))).isTrue();
    }

    @Test
    void finishGame_successfully() {
        Game game = new Game(new GameId(1));

        game.finishGame();

        assertEquals(GameStatus.FINISHED, game.getStatus());
        assertThat(game.getCurrentMoves()).isEmpty();
    }

    @Test
    void deleteGame_successfully() {
        Game game = new Game(new GameId(1));

        game.deleteGame();

        assertEquals(GameStatus.DELETED, game.getStatus());
    }

    @Test
    void canStartGame_withZeroPlayers_false() {
        Game game = new Game(new GameId(1));

        boolean canStart = game.canStartGame();

        assertThat(canStart).isFalse();
    }

    @Test
    void canStartGame_withOnePlayer_false() {
        Game game = new Game(new GameId(1));
        game.addPlayer(new Player(new PlayerId(), new PlayerName("name")));

        boolean canStart = game.canStartGame();

        assertThat(canStart).isFalse();
    }

    @Test
    void canStartGame_withTwoPlayers_true() {
        Game game = new Game(new GameId(1));
        game.addPlayer(new Player(new PlayerId(), new PlayerName("name1")));
        game.addPlayer(new Player(new PlayerId(), new PlayerName("name2")));

        boolean canStart = game.canStartGame();

        assertThat(canStart).isTrue();
    }

    @Test
    void isMoveAllowed_withFinishedGame_returnsFalse() {
        Game game = new Game(new GameId(1));
        game.finishGame();

        boolean canPlayMove = game.isMoveAllowed(new Move(new PlayerId(), new RequestId(), game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(canPlayMove).isFalse();
    }

    @Test
    void isMoveAllowed_withValidMove_returnsTrue() {
        Game game = new Game(new GameId(1));
        PlayerId playerId = new PlayerId();
        game.addPlayer(new Player(playerId, new PlayerName("name1")));
        game.addPlayer(new Player(new PlayerId(), new PlayerName("name2")));
        game.startGame();
        RequestId requestId = getRequestId(game, playerId);

        boolean canPlayMove = game.isMoveAllowed(new Move(playerId, requestId, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(canPlayMove).isTrue();
    }

    @Test
    void isMoveAllowed_withInvalidRequestId_returnsFalse() {
        Game game = new Game(new GameId(1));
        PlayerId playerId = new PlayerId();
        game.addPlayer(new Player(playerId, new PlayerName("name1")));
        game.addPlayer(new Player(new PlayerId(), new PlayerName("name2")));
        game.startGame();

        boolean canPlayMove = game.isMoveAllowed(new Move(playerId, new RequestId(), game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(canPlayMove).isFalse();
    }

    @Test
    void isMoveAllowed_withInvalidPlayerId_returnsFalse() {
        Game game = new Game(new GameId(1));
        PlayerId playerId = new PlayerId();
        game.addPlayer(new Player(playerId, new PlayerName("name1")));
        game.addPlayer(new Player(new PlayerId(), new PlayerName("name2")));
        game.startGame();
        RequestId requestId = getRequestId(game, playerId);

        boolean canPlayMove = game.isMoveAllowed(new Move(new PlayerId(), requestId, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(canPlayMove).isFalse();
    }

    @Test
    void isMoveAllowed_withInValidMove_returnsFalse() {
        Game game = new Game(new GameId(1));
        PlayerId playerId1 = new PlayerId();
        game.addPlayer(new Player(playerId1, new PlayerName("name1")));
        PlayerId playerId2 = new PlayerId();
        game.addPlayer(new Player(playerId2, new PlayerName("name2")));
        game.startGame();
        RequestId requestId = getRequestId(game, playerId1);

        // move where a piece jumps over a different piece
        boolean canPlayMove = game.isMoveAllowed(new Move(playerId1, requestId, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 7))));

        assertThat(canPlayMove).isFalse();
    }

    @Test
    void playMove_withValidMove_updatesStateSuccessfully() {
        Game game = new Game(new GameId(1));
        PlayerId playerId1 = new PlayerId();
        game.addPlayer(new Player(playerId1, new PlayerName("name1")));
        PlayerId playerId2 = new PlayerId();
        game.addPlayer(new Player(playerId2, new PlayerName("name2")));
        game.startGame();
        RequestId requestId1 = getRequestId(game, playerId1);

        game.playMove(new Move(playerId1, requestId1, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(game.getState().currentRequests()).hasSize(1);
        assertThat(game.getState().currentRequests()).noneMatch(request -> request.playerId().equals(playerId1));
        assertThat(game.getState().currentRequests()).anyMatch(request -> request.playerId().equals(playerId2));
        assertThat(game.getCurrentMoves()).hasSize(0);
        assertThat(game.getState().moves()).hasSize(1);
        assertThat(game.getState().moves()).anyMatch(move -> move.playerId().equals(playerId1));
        assertThat(game.getState().moves()).noneMatch(move -> move.playerId().equals(playerId2));
    }



    @Test
    void playMove_attackerWinsByCapturing_finishesGameSuccessfully() {
        Game game = new Game(new GameId(1));
        PlayerId playerId1 = new PlayerId();
        game.addPlayer(new Player(playerId1, new PlayerName("name1")));
        PlayerId playerId2 = new PlayerId();
        game.addPlayer(new Player(playerId2, new PlayerName("name2")));
        game.startGame();

        game.playMove(new Move(playerId1, getRequestId(game, playerId1), game.getGameId(), new GameAction(new Coordinates(3, 0), new Coordinates(0, 0))));
        game.playMove(new Move(playerId2, getRequestId(game, playerId2), game.getGameId(), new GameAction(new Coordinates(3, 4), new Coordinates(3, 0))));
        game.playMove(new Move(playerId1, getRequestId(game, playerId1), game.getGameId(), new GameAction(new Coordinates(3, 8), new Coordinates(3, 4))));
        game.playMove(new Move(playerId2, getRequestId(game, playerId2), game.getGameId(), new GameAction(new Coordinates(4, 3), new Coordinates(6, 3))));
        game.playMove(new Move(playerId1, getRequestId(game, playerId1), game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(4, 3))));
        game.playMove(new Move(playerId2, getRequestId(game, playerId2), game.getGameId(), new GameAction(new Coordinates(4, 5), new Coordinates(6, 5))));
        game.playMove(new Move(playerId1, getRequestId(game, playerId1), game.getGameId(), new GameAction(new Coordinates(0, 5), new Coordinates(4, 5))));
        game.playMove(new Move(playerId2, getRequestId(game, playerId2), game.getGameId(), new GameAction(new Coordinates(5, 4), new Coordinates(5, 2))));
        game.playMove(new Move(playerId1, getRequestId(game, playerId1), game.getGameId(), new GameAction(new Coordinates(5, 8), new Coordinates(5, 4))));
        game.printBoard(); // just for visualization


        assertThat(game.getState().currentRequests()).hasSize(0);
        assertThat(game.getCurrentMoves()).hasSize(0);
        assertThat(game.getState().moves()).hasSize(9);
        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getWinner()).isPresent();
        assertThat(game.getWinner().get()).isEqualTo(playerId1);
    }

    @Test
    void playMove_defenderWinsByEscaping_finishesGameSuccessfully() {
        Game game = new Game(new GameId(1));
        PlayerId playerId1 = new PlayerId();
        game.addPlayer(new Player(playerId1, new PlayerName("name1")));
        PlayerId playerId2 = new PlayerId();
        game.addPlayer(new Player(playerId2, new PlayerName("name2")));

        game.startGame();
        game.playMove(new Move(playerId1, getRequestId(game, playerId1), game.getGameId(), new GameAction(new Coordinates(3, 0), new Coordinates(0, 0))));
        game.playMove(new Move(playerId2, getRequestId(game, playerId2), game.getGameId(), new GameAction(new Coordinates(3, 4), new Coordinates(3, 7))));
        game.playMove(new Move(playerId1, getRequestId(game, playerId1), game.getGameId(), new GameAction(new Coordinates(0, 0), new Coordinates(0, 1))));
        game.playMove(new Move(playerId2, getRequestId(game, playerId2), game.getGameId(), new GameAction(new Coordinates(4, 4), new Coordinates(3, 4))));
        game.playMove(new Move(playerId1, getRequestId(game, playerId1), game.getGameId(), new GameAction(new Coordinates(0, 1), new Coordinates(0, 0))));
        game.playMove(new Move(playerId2, getRequestId(game, playerId2), game.getGameId(), new GameAction(new Coordinates(3, 4), new Coordinates(3, 0))));
        game.printBoard(); // just for visualization

        assertThat(game.getState().currentRequests()).hasSize(0);
        assertThat(game.getCurrentMoves()).hasSize(0);
        assertThat(game.getState().moves()).hasSize(6);
        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getWinner()).isPresent();
        assertThat(game.getWinner().get()).isEqualTo(playerId2);
    }

    private static RequestId getRequestId(Game game, PlayerId playerId1) {
        return game.getState().currentRequests().stream()
                .filter(request -> request.playerId().equals(playerId1))
                .map(PlayRequest::requestId)
                .findFirst()
                .orElse(new RequestId());
    }
}
