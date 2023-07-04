package ch.zuehlke.challenge.bot.client;

import ch.zuehlke.challenge.bot.service.ShutDownService;
import ch.zuehlke.challenge.bot.util.ApplicationProperties;
import ch.zuehlke.common.Coordinates;
import ch.zuehlke.common.GameAction;
import ch.zuehlke.common.GameId;
import ch.zuehlke.common.JoinRequest;
import ch.zuehlke.common.JoinResponse;
import ch.zuehlke.common.Move;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.common.RequestId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameClientTest {

    private GameClient gameClient;
    private RestTemplate restTemplateMock;
    private ApplicationProperties applicationPropertiesMock;
    private ShutDownService shutDownServiceMock;
    private static final String TOKEN = "1111";
    private static final String NAME = "NAME";

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        applicationPropertiesMock = mock(ApplicationProperties.class);
        shutDownServiceMock = mock(ShutDownService.class);
        gameClient = new GameClient(restTemplateMock, applicationPropertiesMock, shutDownServiceMock);

        when(applicationPropertiesMock.getName()).thenReturn(NAME);
    }

    @Test
    void join_successfully() {
        when(applicationPropertiesMock.getBackendJoinUrl()).thenReturn("/lobby/join");

        PlayerName name = new PlayerName(NAME);
        ResponseEntity<JoinResponse> response = ResponseEntity.ok(new JoinResponse(name));
        when(restTemplateMock.postForEntity(any(), any(), eq(JoinResponse.class), anyInt())).thenReturn(response);

        PlayerName actualPlayerName = gameClient.join();

        assertThat(actualPlayerName).isEqualTo(name);
        JoinRequest expectedRequest = new JoinRequest(name);

        verify(restTemplateMock, times(1)).postForEntity("/lobby/join", new HttpEntity<>(expectedRequest), JoinResponse.class, 1);
    }

    @Test
    void join_with400ResponseException_shutsDown() {
        when(applicationPropertiesMock.getBackendJoinUrl()).thenReturn("/lobby/join");

        when(restTemplateMock.postForEntity(any(), any(), eq(JoinResponse.class), anyInt()))
                .thenThrow(new RestClientResponseException("Message", 400, "Status", new HttpHeaders(), null, null));

        PlayerName actualPlayerName = gameClient.join();

        assertThat(actualPlayerName).isNull();
        verify(shutDownServiceMock, times(1)).shutDown();
    }

    @Test
    void play_successfully() {
        when(applicationPropertiesMock.getBackendPlayUrl()).thenReturn("/game/{gameId}/play");
        when(restTemplateMock.postForEntity(any(), any(), eq(Void.class), anyString())).thenReturn(ResponseEntity.ok(null));

        Move move = new Move(new PlayerName(NAME), new RequestId(), new GameId("1"), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        gameClient.play(move);

        verify(restTemplateMock, times(1)).postForEntity("/game/{gameId}/play", new HttpEntity<>(move), Void.class, "1");
    }

    @Test
    void play_with400ResponseException_shutsDown() {
        when(applicationPropertiesMock.getBackendPlayUrl()).thenReturn("/game/{gameId}/play");
        when(restTemplateMock.postForEntity(any(), any(), eq(Void.class), anyString()))
                .thenThrow(new RestClientResponseException("Message", 400, "Status", new HttpHeaders(), null, null));

        Move move = new Move(new PlayerName(NAME), new RequestId(), new GameId("1"), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        gameClient.play(move);

        verify(shutDownServiceMock, times(1)).shutDown();
    }

}
