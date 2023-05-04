package ch.zuehlke.challenge.bot.client;

import ch.zuehlke.challenge.bot.service.ShutDownService;
import ch.zuehlke.challenge.bot.util.ApplicationProperties;
import ch.zuehlke.common.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GameClientTest {

    private GameClient gameClient;
    private RestTemplate restTemplateMock;
    private ApplicationProperties applicationPropertiesMock;
    private ShutDownService shutDownServiceMock;
    private final PlayerName playerName = new PlayerName("name");

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        applicationPropertiesMock = mock(ApplicationProperties.class);
        shutDownServiceMock = mock(ShutDownService.class);
        gameClient = new GameClient(restTemplateMock, applicationPropertiesMock, shutDownServiceMock);
        when(applicationPropertiesMock.getGameId()).thenReturn(1);
        when(applicationPropertiesMock.getName()).thenReturn("name");
        when(applicationPropertiesMock.getToken()).thenReturn("1111");
    }

    @Test
    void join_successfully() {
        when(applicationPropertiesMock.getBackendJoinUrl()).thenReturn("/lobby/join");

        ResponseEntity<JoinResponse> response = ResponseEntity.ok(new JoinResponse(playerName));
        when(restTemplateMock.postForEntity(any(), any(), eq(JoinResponse.class), anyInt())).thenReturn(response);

        PlayerName actualPlayerName = gameClient.join();

        assertThat(actualPlayerName).isEqualTo(playerName);
        JoinRequest expectedRequest = new JoinRequest(new PlayerName("name"));
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", "1111");
        HttpEntity<JoinRequest> httpEntity = new HttpEntity<>(expectedRequest, headers);
        verify(restTemplateMock, times(1)).postForEntity("/lobby/join", httpEntity, JoinResponse.class, 1);
    }

    @Test
    void play_successfully() {
        when(applicationPropertiesMock.getBackendPlayUrl()).thenReturn("/game/{gameId}/play");
        when(restTemplateMock.postForEntity(any(), any(), eq(Void.class), anyInt())).thenReturn(ResponseEntity.ok(null));

        Move move = new Move(playerName, new RequestId(), new GameId(1), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        gameClient.play(move);

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", "1111");
        HttpEntity<Move> httpEntity = new HttpEntity<>(move, headers);

        verify(restTemplateMock, times(1)).postForEntity("/game/{gameId}/play", httpEntity, Void.class, 1);
    }

}