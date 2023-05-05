package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.GameDto;
import ch.zuehlke.common.GameId;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.common.TournamentDto;
import ch.zuehlke.common.TournamentId;
import ch.zuehlke.fullstack.hackathon.model.Game;
import ch.zuehlke.fullstack.hackathon.model.GameMapper;
import ch.zuehlke.fullstack.hackathon.model.TournamentMapper;
import ch.zuehlke.fullstack.hackathon.service.GameService;
import ch.zuehlke.fullstack.hackathon.service.LobbyService;
import ch.zuehlke.fullstack.hackathon.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
@RequiredArgsConstructor
public class PublicController {

    // Improve: Make endpoints secure

    // Improve: Create ExceptionInterceptor for custom exceptions in the backend

    private final LobbyService lobbyService;
    private final GameService gameService;
    private final TournamentService tournamentService;

    @Operation(summary = "Returns a list of all players",
            description = "Returns a list of all players in the lobby")
    @ApiResponse(responseCode = "200", description = "Successfully returned the list of players")
    @GetMapping("/lobby")
    public ResponseEntity<List<PlayerName>> getLobby() {
        List<PlayerName> playerNames = lobbyService.getPlayers();
        return ResponseEntity.ok(playerNames);
    }

    @Operation(summary = "Returns the list of game ids",
            description = "Returns all game ids, whether they are in progress or not")
    @ApiResponse(responseCode = "200", description = "Successfully returned the list of game ids")
    @GetMapping("/games")
    public ResponseEntity<List<GameId>> getGameIds() {
        List<Game> games = gameService.getGames();
        List<GameId> gameIds = games.stream()
                .map(Game::getGameId)
                .toList();
        return ResponseEntity.ok(gameIds);
    }

    @Operation(summary = "Returns the game",
            description = "Returns the game, whether it is in progress or not")
    @ApiResponse(responseCode = "200", description = "Successfully returned the game")
    @ApiResponse(responseCode = "404", description = "Could not find the game with the given id")
    @GetMapping("/game/{gameId}")
    public ResponseEntity<GameDto> getGame(@PathVariable int gameId) {
        Optional<Game> game = gameService.getGame(new GameId(gameId));
        return game.map(GameMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Returns the list of tournaments",
            description = "Returns all tournaments, whether they are in progress or not")
    @ApiResponse(responseCode = "200", description = "Successfully returned the list of tournaments")
    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentDto>> getTournaments() {
        var tournaments = tournamentService.getTournaments();
        var dtos = tournaments.stream()
                .map(tournament -> TournamentMapper.map(tournament, gameService.getGames(tournament.getGameIds())))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Returns the tournament for a given id",
            description = "Returns all tournaments, whether they are in progress or not")
    @ApiResponse(responseCode = "200", description = "Successfully returned the tournament")
    @ApiResponse(responseCode = "404", description = "Did not find the tournament")
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<TournamentDto> getTournament(@PathVariable int tournamentId) {
        return tournamentService.getTournament(new TournamentId(tournamentId))
                .map(tournament -> TournamentMapper.map(tournament, gameService.getGames(tournament.getGameIds())))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
