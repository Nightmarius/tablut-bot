package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.*;
import ch.zuehlke.fullstack.hackathon.service.BotService;
import ch.zuehlke.fullstack.hackathon.service.GameService;
import ch.zuehlke.fullstack.hackathon.service.NotificationService;
import ch.zuehlke.fullstack.hackathon.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BotControllerTest {

    private BotController botController;
    private BotService botServiceMock;
    private GameService gameServiceMock;
    private TournamentService tournamentServiceMock;
    private NotificationService notificationServiceMock;
    private final PlayerName bestBot = new PlayerName("bestBot");
    private final Token bestToken = new Token("11111111111111111111111111111111");
    private final PlayerName fakeBot = new PlayerName("fakeBot");
    private final Token fakeToken = new Token("1234567890abcdefghijklmnopqrstuv");
    private final GameId gameId = new GameId(42);

    @BeforeEach
    void setUp() {
        botServiceMock = mock(BotService.class);
        gameServiceMock = mock(GameService.class);
        tournamentServiceMock = mock(TournamentService.class);
        notificationServiceMock = mock(NotificationService.class);
        botController = new BotController(botServiceMock, gameServiceMock, tournamentServiceMock, notificationServiceMock);
        BotDto bestBotDto = new BotDto(bestBot, bestToken);
        when(botServiceMock.getBot(fakeBot)).thenReturn(Optional.empty());
        when(botServiceMock.getBot(bestBot)).thenReturn(Optional.of(bestBotDto));
    }

    @Test
    void join_successfully() {
        JoinResult joinResult = new JoinResult(new PlayerId(), JoinResult.JoinResultType.SUCCESS);
        when(gameServiceMock.join(anyInt(), any())).thenReturn(joinResult);

        PlayerName playerName = bestBot;
        JoinRequest joinRequest = new JoinRequest(playerName);
        ResponseEntity<JoinResponse> response = botController.joinGame(gameId.value(), bestToken.value(), joinRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(new JoinResponse(joinResult.playerId()));
        verify(gameServiceMock, times(1)).join(gameId.value(), playerName);
        verify(notificationServiceMock, times(1)).notifyGameUpdate(gameId);
    }

    @Test
    void join_wrongNameReturns403() {
        JoinRequest joinRequest = new JoinRequest(fakeBot);
        ResponseEntity<JoinResponse> response = botController.joinGame(gameId.value(), bestToken.value(), joinRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void join_wrongTokenReturns403() {
        JoinRequest joinRequest = new JoinRequest(bestBot);
        ResponseEntity<JoinResponse> response = botController.joinGame(gameId.value(), fakeToken.value(), joinRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void join_whenGameIsFull_returns400() {
        JoinResult joinResult = new JoinResult(null, JoinResult.JoinResultType.GAME_FULL);
        when(gameServiceMock.join(anyInt(), any())).thenReturn(joinResult);

        PlayerName playerName = bestBot;
        JoinRequest joinRequest = new JoinRequest(playerName);
        ResponseEntity<JoinResponse> response = botController.joinGame(gameId.value(), bestToken.value(), joinRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
        assertThat(response.getBody()).isNull();
        verify(gameServiceMock, times(1)).join(gameId.value(), playerName);
        verify(notificationServiceMock, never()).notifyGameUpdate(any());
    }

    @Test
    void join_whenGameIsNotFound_returns404() {
        JoinResult joinResult = new JoinResult(null, JoinResult.JoinResultType.GAME_NOT_FOUND);
        when(gameServiceMock.join(anyInt(), any())).thenReturn(joinResult);

        PlayerName playerName = bestBot;
        JoinRequest joinRequest = new JoinRequest(playerName);
        ResponseEntity<JoinResponse> response = botController.joinGame(gameId.value(), bestToken.value(), joinRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
        assertThat(response.getBody()).isNull();
        verify(gameServiceMock, times(1)).join(gameId.value(), playerName);
        verify(notificationServiceMock, never()).notifyGameUpdate(any());
    }

    @Test
    void play_successfully() {
        Move move = new Move(new PlayerId(), new RequestId(), gameId, new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));

        when(gameServiceMock.play(eq(move), eq(gameId))).thenReturn(new PlayResult(PlayResult.PlayResultType.SUCCESS));

        ResponseEntity<Void> response = botController.play(gameId.value(), bestToken.value(), move);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isNull();
        verify(notificationServiceMock, times(1)).notifyGameUpdate(gameId);
    }

    @Test
    void play_whenGameIsNotFound_returns404() {
        Move move = new Move(new PlayerId(), new RequestId(), gameId, new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        when(gameServiceMock.play(eq(move), eq(gameId))).thenReturn(new PlayResult(PlayResult.PlayResultType.GAME_NOT_FOUND));

        ResponseEntity<Void> response = botController.play(gameId.value(), bestToken.value(), move);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
        assertThat(response.getBody()).isNull();
        verify(notificationServiceMock, never()).notifyGameUpdate(any());
    }

    @Test
    void play_whenPlayerIsNotPartOfTheGame_returns400() {
        Move move = new Move(new PlayerId(), new RequestId(), gameId, new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        when(gameServiceMock.play(eq(move), eq(gameId))).thenReturn(new PlayResult(PlayResult.PlayResultType.PLAYER_NOT_PART_OF_GAME));

        ResponseEntity<Void> response = botController.play(gameId.value(), bestToken.value(), move);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
        assertThat(response.getBody()).isNull();
        verify(notificationServiceMock, never()).notifyGameUpdate(any());
    }

    @Test
    void play_whenActionIsInvalid_returns400() {
        Move move = new Move(new PlayerId(), new RequestId(), gameId, new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        when(gameServiceMock.play(eq(move), eq(gameId))).thenReturn(new PlayResult(PlayResult.PlayResultType.INVALID_ACTION));

        ResponseEntity<Void> response = botController.play(gameId.value(), bestToken.value(), move);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
        assertThat(response.getBody()).isNull();
        verify(notificationServiceMock, never()).notifyGameUpdate(any());
    }
}
