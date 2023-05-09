package ch.zuehlke.challenge.bot.brain;

import ch.zuehlke.challenge.bot.client.GameClient;
import ch.zuehlke.challenge.bot.service.GameService;
import ch.zuehlke.common.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameService gameService;
    private GameClient gameClientMock;
    private Bot botMock;
    private final PlayerName playerName = new PlayerName("name1");

    @BeforeEach
    void setUp() {
        gameClientMock = mock(GameClient.class);
        botMock = mock(Bot.class);
        gameService = new GameService(botMock, gameClientMock);
    }

    @Test
    void joinGame_callGameClient_successfully() {
        when(gameClientMock.join()).thenReturn(playerName);
        gameService.join();

        assertThat(gameService.getPlayerName()).isEqualTo(playerName);
        verify(gameClientMock, times(1)).join();
    }

    @Test
    void onGameUpdate_withImportantUpdate_playsMove() {
        var board = Board.createInitialBoard();
        GameState state = new GameState(Set.of(new PlayRequest(playerName, new GameId(1), true, board, Set.of())), List.of());
        GameDto gameDto = new GameDto(new GameId(1), List.of(), GameStatus.IN_PROGRESS, state, null);
        when(botMock.decide(anyBoolean(), any(), any())).thenReturn(new GameAction(new Coordinates(0, 0), new Coordinates(0, 0)));

        gameService.setPlayerName(playerName);
        gameService.onGameUpdate(gameDto);

        verify(botMock, times(1)).decide(true, board, Set.of());
        verify(gameClientMock, times(1)).play(any());
    }

    @Test
    void onGameUpdate_whenGameIsNotStarted_doesNotPlayMove() {
        GameState state = new GameState(Set.of(), List.of());
        GameDto gameDto = new GameDto(new GameId(1), List.of(), GameStatus.NOT_STARTED, state, null);
        when(botMock.decide(anyBoolean(), any(), any())).thenReturn(new GameAction(new Coordinates(0, 0), new Coordinates(0, 0)));

        gameService.setPlayerName(playerName);
        gameService.onGameUpdate(gameDto);

        verify(botMock, never()).decide(anyBoolean(), any(), any());
        verify(gameClientMock, never()).play(any());
    }

    @Test
    void onGameUpdate_whenGameIsFinished_doesNotPlayMove() {
        GameState state = new GameState(Set.of(), List.of());
        GameDto gameDto = new GameDto(new GameId(1), List.of(), GameStatus.FINISHED, state, null);
        when(botMock.decide(anyBoolean(), any(), any())).thenReturn(new GameAction(new Coordinates(0, 0), new Coordinates(0, 0)));

        gameService.setPlayerName(playerName);
        gameService.onGameUpdate(gameDto);

        verify(botMock, never()).decide(anyBoolean(), any(), any());
        verify(gameClientMock, never()).play(any());
    }

    @Test
    void onGameUpdate_whenCalledTwice_onlyPlaysOneMove() {
        var board = Board.createInitialBoard();
        GameState state = new GameState(Set.of(new PlayRequest(playerName, new GameId(1), true, board, Set.of())), List.of());
        GameDto gameDto = new GameDto(new GameId(1), List.of(), GameStatus.IN_PROGRESS, state, null);
        when(botMock.decide(anyBoolean(), any(), any())).thenReturn(new GameAction(new Coordinates(0, 0), new Coordinates(0, 0)));

        gameService.setPlayerName(playerName);
        gameService.onGameUpdate(gameDto);
        gameService.onGameUpdate(gameDto); // call twice

        verify(botMock, times(1)).decide(true, board, Set.of());
        verify(gameClientMock, times(1)).play(any());
    }

    @Test
    void onGameUpdate_whenItsForADifferentPlayer_doesNotPlayMove() {
        GameState state = new GameState(Set.of(new PlayRequest(playerName, new GameId(1), true, Board.createInitialBoard(), Set.of())), List.of());
        GameDto gameDto = new GameDto(new GameId(1), List.of(), GameStatus.IN_PROGRESS, state, null);
        when(botMock.decide(anyBoolean(), any(), any())).thenReturn(new GameAction(new Coordinates(0, 0), new Coordinates(0, 0)));

        gameService.setPlayerName(new PlayerName("name2"));
        gameService.onGameUpdate(gameDto);

        verify(botMock, never()).decide(anyBoolean(), any(), any());
        verify(gameClientMock, never()).play(any());
    }
}