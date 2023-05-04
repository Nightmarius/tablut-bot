package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.GameDto;
import ch.zuehlke.common.GameId;
import ch.zuehlke.fullstack.hackathon.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private NotificationService notificationService;
    private SimpMessagingTemplate simpMessagingTemplateMock;
    private LobbyService lobbyServiceMock;
    private GameService gameServiceMock;
    private TournamentService tournamentService;

    @BeforeEach
    void setUp() {
        this.simpMessagingTemplateMock = mock(SimpMessagingTemplate.class);
        this.lobbyServiceMock = mock(LobbyService.class);
        this.gameServiceMock = mock(GameService.class);
        this.tournamentService = mock(TournamentService.class);
        this.notificationService = new NotificationService(simpMessagingTemplateMock, lobbyServiceMock, gameServiceMock, tournamentService);
    }

    @Test
    void notifyGameUpdate_withExistingGame_successfully() {
        int gameIdValue = 1;
        Game game = new Game(new GameId(gameIdValue));
        when(gameServiceMock.getGame(gameIdValue)).thenReturn(Optional.of(game));

        notificationService.notifyGameUpdate(game.getGameId());

        verify(simpMessagingTemplateMock).convertAndSend(eq("/topic/game/"), any(GameDto.class));
        verify(gameServiceMock).getGame(gameIdValue);
    }

    @Test
    void notifyGameUpdate_whenGameDoesNotExist_doesNotSendUpdate() {
        int gameIdValue = 1;
        when(gameServiceMock.getGame(gameIdValue)).thenReturn(Optional.empty());

        notificationService.notifyGameUpdate(new GameId(gameIdValue));

        verify(simpMessagingTemplateMock, never()).convertAndSend(anyString(), (Object) any());
        verify(gameServiceMock).getGame(gameIdValue);
    }
}
