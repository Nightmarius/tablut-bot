package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.GameId;
import ch.zuehlke.fullstack.hackathon.service.GameService;
import ch.zuehlke.fullstack.hackathon.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PublicControllerTest {

    private PublicController publicController;
    private GameService gameServiceMock;
    private TournamentService tournamentServiceMock;

    @BeforeEach
    void setUp() {
        gameServiceMock = mock(GameService.class);
        tournamentServiceMock = mock(TournamentService.class);
        publicController = new PublicController(gameServiceMock, tournamentServiceMock);
    }

    @Test
    void getGames_emptyList_successfully() {
        when(gameServiceMock.getGames()).thenReturn(List.of());

        ResponseEntity<List<GameId>> response = publicController.getGameIds();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(List.of());
        verify(gameServiceMock, times(1)).getGames();
    }

}