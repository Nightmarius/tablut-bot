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
    private GameService gameServiceMock;
    private TournamentService tournamentService;
    private final GameId gameId = new GameId(42);
    private final Game game = new Game(gameId);

    @BeforeEach
    void setUp() {
        this.simpMessagingTemplateMock = mock(SimpMessagingTemplate.class);
        this.gameServiceMock = mock(GameService.class);
        this.tournamentService = mock(TournamentService.class);
        this.notificationService = new NotificationService(simpMessagingTemplateMock, gameServiceMock, tournamentService);
    }

    @Test
    void notifyGameUpdate_withExistingGame_successfully() {
        when(gameServiceMock.getGame(gameId)).thenReturn(Optional.of(game));

        notificationService.notifyGameUpdate(game.getGameId());

        verify(simpMessagingTemplateMock).convertAndSend(eq("/topic/game/"), any(GameDto.class));
        verify(gameServiceMock).getGame(gameId);
    }

    @Test
    void notifyGameUpdate_whenGameDoesNotExist_doesNotSendUpdate() {
        when(gameServiceMock.getGame(gameId)).thenReturn(Optional.empty());

        notificationService.notifyGameUpdate(gameId);

        verify(simpMessagingTemplateMock, never()).convertAndSend(anyString(), (Object) any());
        verify(gameServiceMock).getGame(gameId);
    }
}
