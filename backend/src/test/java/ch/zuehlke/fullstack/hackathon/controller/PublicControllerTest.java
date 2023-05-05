package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.GameDto;
import ch.zuehlke.common.GameId;
import ch.zuehlke.common.TournamentDto;
import ch.zuehlke.common.TournamentId;
import ch.zuehlke.fullstack.hackathon.model.Game;
import ch.zuehlke.fullstack.hackathon.model.Tournament;
import ch.zuehlke.fullstack.hackathon.service.GameService;
import ch.zuehlke.fullstack.hackathon.service.LobbyService;
import ch.zuehlke.fullstack.hackathon.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PublicControllerTest {

    private PublicController publicController;
    private LobbyService lobbyServiceMock;
    private GameService gameServiceMock;
    private TournamentService tournamentServiceMock;

    @BeforeEach
    void setUp() {
        lobbyServiceMock = mock(LobbyService.class);
        gameServiceMock = mock(GameService.class);
        tournamentServiceMock = mock(TournamentService.class);
        publicController = new PublicController(lobbyServiceMock, gameServiceMock, tournamentServiceMock);
    }

    @Test
    void getGameIds_emptyList_successfully() {
        when(gameServiceMock.getGames()).thenReturn(List.of());

        ResponseEntity<List<GameId>> response = publicController.getGameIds();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(List.of());
        verify(gameServiceMock, times(1)).getGames();
    }

    @Test
    void getGame_successfully() {
        GameId gameId = new GameId(42);
        Game game = new Game(gameId);
        when(gameServiceMock.getGame(gameId)).thenReturn(Optional.of(game));

        ResponseEntity<GameDto> response = publicController.getGame(42);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(Objects.requireNonNull(response.getBody()).id()).isEqualTo(gameId);
        verify(gameServiceMock, times(1)).getGame(gameId);
    }

    @Test
    void getTournaments_successfully() {
        when(tournamentServiceMock.getTournaments()).thenReturn(List.of());

        ResponseEntity<List<TournamentDto>> response = publicController.getTournaments();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(List.of());
        verify(tournamentServiceMock, times(1)).getTournaments();
    }

    @Test
    void getTournament_successfully() {
        TournamentId tournamentId = new TournamentId(42);
        Tournament tournament = new Tournament(tournamentId);
        when(tournamentServiceMock.getTournament(tournamentId)).thenReturn(Optional.of(tournament));

        ResponseEntity<TournamentDto> response = publicController.getTournament(42);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(Objects.requireNonNull(response.getBody()).id()).isEqualTo(tournamentId);
        verify(tournamentServiceMock, times(1)).getTournament(tournamentId);
    }
}