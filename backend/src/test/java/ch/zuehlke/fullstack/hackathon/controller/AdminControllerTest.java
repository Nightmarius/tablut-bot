package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.BotDto;
import ch.zuehlke.common.GameId;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.common.Token;
import ch.zuehlke.fullstack.hackathon.model.Game;
import ch.zuehlke.fullstack.hackathon.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AdminControllerTest {
    private AdminController adminController;
    private AdminService adminServiceMock;
    private GameService gameServiceMock;
    private TournamentService tournamentServiceMock;
    private BotService botServiceMock;
    private NotificationService notificationServiceMock;
    private final PlayerName bestBot = new PlayerName("bestBot");
    private final Token bestToken = new Token("11111111111111111111111111111111");
    private final GameId gameId = new GameId(42);

    @BeforeEach
    void setUp() {
        adminServiceMock = mock(AdminService.class);
        gameServiceMock = mock(GameService.class);
        tournamentServiceMock = mock(TournamentService.class);
        botServiceMock = mock(BotService.class);
        notificationServiceMock = mock(NotificationService.class);
        adminController = new AdminController(adminServiceMock, gameServiceMock, tournamentServiceMock, botServiceMock, notificationServiceMock);
        List<BotDto> bots = new ArrayList<BotDto>();
        bots.add(new BotDto(bestBot, bestToken));
        when(botServiceMock.getBots()).thenReturn(bots);
    }

    @Test
    void getBots_successfully() {
        List<BotDto> bots = adminController.getBots().getBody();

        assertThat(bots).hasSize(1);
        assertThat(bots.get(0).name()).isEqualTo(bestBot);
        assertThat(bots.get(0).token()).isEqualTo(bestToken);
    }

    @Test
    void generate_successfully() {
        adminController.generate(bestBot);
        verify(botServiceMock).addBot(bestBot);
    }

    @Test
    void createGame_successfully() {
        when(gameServiceMock.createGame()).thenReturn(new Game(gameId));

        ResponseEntity<GameId> response = adminController.createGame();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(gameId);
        verify(gameServiceMock, times(1)).createGame();
    }

    @Test
    void deleteGame_successfully() {
        when(gameServiceMock.deleteGame(anyInt())).thenReturn(true);

        ResponseEntity<Void> response = adminController.deleteGame(gameId.value());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteGame_whenGameDidNotExist_returns404() {
        when(gameServiceMock.deleteGame(anyInt())).thenReturn(false);

        ResponseEntity<Void> response = adminController.deleteGame(gameId.value());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
        assertThat(response.getBody()).isNull();
    }

    @Test
    void startGame_successfully() {
        when(gameServiceMock.startGame(anyInt())).thenReturn(new StartResult(StartResult.StartResultType.SUCCESS));

        ResponseEntity<Void> response = adminController.startGame(gameId.value());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isNull();
        verify(notificationServiceMock, times(1)).notifyGameUpdate(gameId);
    }

    @Test
    void startGame_whenGameIsNotFound_returns404() {
        when(gameServiceMock.startGame(anyInt())).thenReturn(new StartResult(StartResult.StartResultType.GAME_NOT_FOUND));

        ResponseEntity<Void> response = adminController.startGame(gameId.value());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
        assertThat(response.getBody()).isNull();
        verify(notificationServiceMock, never()).notifyGameUpdate(any());

    }

    @Test
    void startGame_whenGameHasNotEnoughPlayers_returns400() {
        when(gameServiceMock.startGame(anyInt())).thenReturn(new StartResult(StartResult.StartResultType.NOT_ENOUGH_PLAYERS));

        ResponseEntity<Void> response = adminController.startGame(gameId.value());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
        assertThat(response.getBody()).isNull();
        verify(notificationServiceMock, never()).notifyGameUpdate(any());
    }

}
