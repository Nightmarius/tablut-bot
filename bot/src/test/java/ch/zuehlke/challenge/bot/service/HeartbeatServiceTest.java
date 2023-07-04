package ch.zuehlke.challenge.bot.service;

import ch.zuehlke.challenge.bot.util.ApplicationProperties;
import ch.zuehlke.common.HeartbeatRequest;
import ch.zuehlke.common.PlayerName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class HeartbeatServiceTest {


    private HeartbeatService heartbeatService;

    private RestTemplate restTemplateMock;

    private ApplicationProperties applicationPropertiesMock;

    private GameService gameServiceMock;

    private ShutDownService shutDownServiceMock;

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        applicationPropertiesMock = mock(ApplicationProperties.class);
        gameServiceMock = mock(GameService.class);
        shutDownServiceMock = mock(ShutDownService.class);
        heartbeatService = new HeartbeatService(restTemplateMock, applicationPropertiesMock, gameServiceMock, shutDownServiceMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(restTemplateMock, applicationPropertiesMock, gameServiceMock, shutDownServiceMock);
    }

    @Test
    void sendHeartbeat_successfully() {
        when(applicationPropertiesMock.getBackendHeartbeatUrl()).thenReturn("http://localhost:8080/heartbeat");
        PlayerName playerName = new PlayerName("player1");
        when(gameServiceMock.getPlayerName()).thenReturn(playerName);
        when(applicationPropertiesMock.getName()).thenReturn(playerName.value());
        when(restTemplateMock.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok().build());

        heartbeatService.sendHeartbeat();

        verify(applicationPropertiesMock).getBackendHeartbeatUrl();
        verify(applicationPropertiesMock).getName();
        verify(gameServiceMock).getPlayerName();
        verify(restTemplateMock).postForEntity("http://localhost:8080/heartbeat", new HttpEntity<>(new HeartbeatRequest(playerName)), Void.class);
    }

    @Test
    void sendHeartbeat_withNoPlayerNameStoredInGameService_doesNotSendHeartbeat() {
        when(gameServiceMock.getPlayerName()).thenReturn(null);

        heartbeatService.sendHeartbeat();

        verify(gameServiceMock).getPlayerName();
    }

    @Test
    void sendHeartbeat_withClientException_shutsDown() {
        when(applicationPropertiesMock.getBackendHeartbeatUrl()).thenReturn("http://localhost:8080/heartbeat");
        PlayerName playerName = new PlayerName("player1");
        when(gameServiceMock.getPlayerName()).thenReturn(playerName);
        when(applicationPropertiesMock.getName()).thenReturn(playerName.value());
        when(restTemplateMock.postForEntity(anyString(), any(), any())).thenThrow(new RestClientException("Something failed"));

        heartbeatService.sendHeartbeat();

        verify(applicationPropertiesMock).getBackendHeartbeatUrl();
        verify(applicationPropertiesMock).getName();
        verify(gameServiceMock).getPlayerName();
        verify(restTemplateMock).postForEntity("http://localhost:8080/heartbeat", new HttpEntity<>(new HeartbeatRequest(playerName)), Void.class);
        verify(shutDownServiceMock).shutDown();
    }
}
