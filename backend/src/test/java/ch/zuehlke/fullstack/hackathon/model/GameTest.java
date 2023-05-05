package ch.zuehlke.fullstack.hackathon.model;

import ch.zuehlke.common.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    PlayerName player1 = new PlayerName("name1");
    PlayerName player2 = new PlayerName("name2");

    @Test
    void addPlayer_successfully() {
        Game game = new Game(new GameId(1));

        game.addPlayer(player1);

        assertThat(game.getPlayers()).hasSize(1);
        assertThat(game.getPlayers().get(0)).isEqualTo(player1);
    }

    @Test
    void startGame_withTwoPlayers_successfully() {
        Game game = new Game(new GameId(1));
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.startGame();

        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertThat(game.getState().currentRequests()).hasSize(1);
        assertThat(game.getState().currentRequests().stream().anyMatch(request -> request.playerName().equals(player1))).isTrue();
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
        game.addPlayer(player1);

        boolean canStart = game.canStartGame();

        assertThat(canStart).isFalse();
    }

    @Test
    void canStartGame_withTwoPlayers_true() {
        Game game = new Game(new GameId(1));
        game.addPlayer(player1);
        game.addPlayer(player2);

        boolean canStart = game.canStartGame();

        assertThat(canStart).isTrue();
    }

    @Test
    void isMoveAllowed_withFinishedGame_returnsFalse() {
        Game game = new Game(new GameId(1));
        game.finishGame();

        boolean canPlayMove = game.isMoveAllowed(new Move(player1, new RequestId(), game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(canPlayMove).isFalse();
    }

    @Test
    void isMoveAllowed_withValidMove_returnsTrue() {
        Game game = new Game(new GameId(1));
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.startGame();
        RequestId requestId = getRequestId(game, player1);

        boolean canPlayMove = game.isMoveAllowed(new Move(player1, requestId, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(canPlayMove).isTrue();
    }

    @Test
    void isMoveAllowed_withInvalidRequestId_returnsFalse() {
        Game game = new Game(new GameId(1));
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.startGame();

        boolean canPlayMove = game.isMoveAllowed(new Move(player1, new RequestId(), game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(canPlayMove).isFalse();
    }

    @Test
    void isMoveAllowed_withInvalidPlayerName_returnsFalse() {
        Game game = new Game(new GameId(1));
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.startGame();
        RequestId requestId = getRequestId(game, player1);

        boolean canPlayMove = game.isMoveAllowed(new Move(player2, requestId, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(canPlayMove).isFalse();
    }

    @Test
    void isMoveAllowed_withInValidMove_returnsFalse() {
        Game game = new Game(new GameId(1));
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.startGame();
        RequestId requestId = getRequestId(game, player1);

        // move where a piece jumps over a different piece
        boolean canPlayMove = game.isMoveAllowed(new Move(player1, requestId, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 7))));

        assertThat(canPlayMove).isFalse();
    }

    @Test
    void playMove_withValidMove_updatesStateSuccessfully() {
        Game game = new Game(new GameId(1));
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.startGame();
        RequestId requestId1 = getRequestId(game, player1);

        game.playMove(new Move(player1, requestId1, game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0))));

        assertThat(game.getState().currentRequests()).hasSize(1);
        assertThat(game.getState().currentRequests()).noneMatch(request -> request.playerName().equals(player1));
        assertThat(game.getState().currentRequests()).anyMatch(request -> request.playerName().equals(player2));
        assertThat(game.getCurrentMoves()).hasSize(0);
        assertThat(game.getState().moves()).hasSize(1);
        assertThat(game.getState().moves()).anyMatch(move -> move.name().equals(player1));
        assertThat(game.getState().moves()).noneMatch(move -> move.name().equals(player2));
    }


    @Test
    void playMove_attackerWinsByCapturing_finishesGameSuccessfully() {
        Game game = new Game(new GameId(1));
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.startGame();

        game.playMove(new Move(player1, getRequestId(game, player1), game.getGameId(), new GameAction(new Coordinates(3, 0), new Coordinates(0, 0))));
        game.playMove(new Move(player2, getRequestId(game, player2), game.getGameId(), new GameAction(new Coordinates(3, 4), new Coordinates(3, 0))));
        game.playMove(new Move(player1, getRequestId(game, player1), game.getGameId(), new GameAction(new Coordinates(3, 8), new Coordinates(3, 4))));
        game.playMove(new Move(player2, getRequestId(game, player2), game.getGameId(), new GameAction(new Coordinates(4, 3), new Coordinates(6, 3))));
        game.playMove(new Move(player1, getRequestId(game, player1), game.getGameId(), new GameAction(new Coordinates(0, 3), new Coordinates(4, 3))));
        game.playMove(new Move(player2, getRequestId(game, player2), game.getGameId(), new GameAction(new Coordinates(4, 5), new Coordinates(6, 5))));
        game.playMove(new Move(player1, getRequestId(game, player1), game.getGameId(), new GameAction(new Coordinates(0, 5), new Coordinates(4, 5))));
        game.playMove(new Move(player2, getRequestId(game, player2), game.getGameId(), new GameAction(new Coordinates(5, 4), new Coordinates(5, 2))));
        game.playMove(new Move(player1, getRequestId(game, player1), game.getGameId(), new GameAction(new Coordinates(5, 8), new Coordinates(5, 4))));
        game.printBoard(); // just for visualization


        assertThat(game.getState().currentRequests()).hasSize(0);
        assertThat(game.getCurrentMoves()).hasSize(0);
        assertThat(game.getState().moves()).hasSize(9);
        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getWinner()).isPresent();
        assertThat(game.getWinner().get()).isEqualTo(player1);
    }

    @Test
    void playMove_defenderWinsByEscaping_finishesGameSuccessfully() {
        Game game = new Game(new GameId(1));
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.startGame();
        game.playMove(new Move(player1, getRequestId(game, player1), game.getGameId(), new GameAction(new Coordinates(3, 0), new Coordinates(0, 0))));
        game.playMove(new Move(player2, getRequestId(game, player2), game.getGameId(), new GameAction(new Coordinates(3, 4), new Coordinates(3, 7))));
        game.playMove(new Move(player1, getRequestId(game, player1), game.getGameId(), new GameAction(new Coordinates(0, 0), new Coordinates(0, 1))));
        game.playMove(new Move(player2, getRequestId(game, player2), game.getGameId(), new GameAction(new Coordinates(4, 4), new Coordinates(3, 4))));
        game.playMove(new Move(player1, getRequestId(game, player1), game.getGameId(), new GameAction(new Coordinates(0, 1), new Coordinates(0, 0))));
        game.playMove(new Move(player2, getRequestId(game, player2), game.getGameId(), new GameAction(new Coordinates(3, 4), new Coordinates(3, 0))));
        game.printBoard(); // just for visualization

        assertThat(game.getState().currentRequests()).hasSize(0);
        assertThat(game.getCurrentMoves()).hasSize(0);
        assertThat(game.getState().moves()).hasSize(6);
        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getWinner()).isPresent();
        assertThat(game.getWinner().get()).isEqualTo(player2);
    }

    private static RequestId getRequestId(Game game, PlayerName player1) {
        return game.getState().currentRequests().stream()
                .filter(request -> request.playerName().equals(player1))
                .map(PlayRequest::requestId)
                .findFirst()
                .orElse(new RequestId());
    }
}
