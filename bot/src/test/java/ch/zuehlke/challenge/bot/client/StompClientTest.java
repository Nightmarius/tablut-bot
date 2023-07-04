package ch.zuehlke.challenge.bot.client;

import ch.zuehlke.challenge.bot.service.GameService;
import ch.zuehlke.challenge.bot.util.ApplicationProperties;
import ch.zuehlke.common.GameDto;
import ch.zuehlke.common.GameId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class StompClientTest {

    private StompClient stompClient;

    private ApplicationProperties applicationPropertiesMock;

    private GameService gameServiceMock;

    @BeforeEach
    void setUp() {
        applicationPropertiesMock = mock(ApplicationProperties.class);
        gameServiceMock = mock(GameService.class);
        stompClient = new StompClient(applicationPropertiesMock, gameServiceMock);
    }

    @Test
    void handleFrame_withNewGameDto_updatesGameSuccessfully() {
        GameDto gameDto = new GameDto(new GameId("1"), new ArrayList<>(), null, null, null);
        stompClient.handleFrame(null, gameDto);

        verify(gameServiceMock, times(1)).onGameUpdate(gameDto);
    }

    @Test
    void handleFrame_withNullValue_updatesGameSuccessfully() {
        stompClient.handleFrame(null, null);

        verify(gameServiceMock, times(1)).onGameUpdate(null);
    }
}
