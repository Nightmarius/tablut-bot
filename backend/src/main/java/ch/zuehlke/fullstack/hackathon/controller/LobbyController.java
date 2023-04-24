package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.*;
import ch.zuehlke.fullstack.hackathon.controller.PlayResult.PlayResultType;
import ch.zuehlke.fullstack.hackathon.model.Game;
import ch.zuehlke.fullstack.hackathon.model.GameMapper;
import ch.zuehlke.fullstack.hackathon.model.Tournament;
import ch.zuehlke.fullstack.hackathon.model.TournamentMapper;
import ch.zuehlke.fullstack.hackathon.service.GameService;
import ch.zuehlke.fullstack.hackathon.service.NotificationService;
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
@RequestMapping("/api/lobby")
@RequiredArgsConstructor
public class LobbyController {

    // Improve: Make endpoints secure

    // Improve: Create ExceptionInterceptor for custom exceptions in the backend

    private final GameService gameService;
    private final TournamentService tournamentService;

    private final NotificationService notificationService;

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
        Optional<Game> game = gameService.getGame(gameId);
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
        return tournamentService.getTournament(tournamentId)
                .map(tournament -> TournamentMapper.map(tournament, gameService.getGames(tournament.getGameIds())))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Creates a new game",
            description = "Creates a new game and returns the game id")
    @ApiResponse(responseCode = "200", description = "Successfully created a new game")
    @PostMapping("/game/{gameId}")
    public ResponseEntity<GameId> createGame() {
        Game game = gameService.createGame();
        return ResponseEntity.ok(game.getGameId());
    }

    @Operation(summary = "Creates a new tournament",
            description = "Creates a new tournament and returns tournament id")
    @ApiResponse(responseCode = "200", description = "Successfully created a new tournament")
    @PostMapping("/tournament")
    public ResponseEntity<TournamentId> createTournament() {
        Tournament tournament = tournamentService.createTournament();
        return ResponseEntity.ok(tournament.getTournamentId());
    }

    @Operation(summary = "Joins a game",
            description = "Joins a game and returns the socket url")
    @ApiResponse(responseCode = "200", description = "Successfully joined the game")
    @ApiResponse(responseCode = "400", description = "Game is already full")
    @ApiResponse(responseCode = "404", description = "The game does not exist")
    @PostMapping("/game/{gameId}/join")
    public ResponseEntity<JoinResponse> join(@PathVariable int gameId, @RequestBody JoinRequest joinRequest) {
        JoinResult joinResult = gameService.join(gameId, joinRequest.name());

        if (joinResult.resultType() == JoinResult.JoinResultType.GAME_NOT_FOUND) {
            return ResponseEntity.notFound().build();
        }
        if (joinResult.resultType() == JoinResult.JoinResultType.GAME_FULL) {
            return ResponseEntity.badRequest().build();
        }
        notificationService.notifyGameUpdate(new GameId(gameId));
        return ResponseEntity.ok(new JoinResponse(joinResult.playerId()));
    }

    @Operation(summary = "Joins a tournament",
            description = "Joins a tournament and returns the socket url")
    @ApiResponse(responseCode = "200", description = "Successfully joined the tournament")
    @ApiResponse(responseCode = "400", description = "Tournament is already full")
    @ApiResponse(responseCode = "404", description = "The tournament does not exist")
    @PostMapping("/tournament/{tournamentId}/join")
    public ResponseEntity<JoinResponse> joinTournament(@PathVariable int tournamentId, @RequestBody JoinRequest joinRequest) {
        var joinResult = tournamentService.join(tournamentId, joinRequest.name());

        if (joinResult.resultType() == TournamentJoinResult.TournamentJoinResultType.TOURNAMENT_NOT_FOUND) {
            return ResponseEntity.notFound().build();
        }
        if (joinResult.resultType() == TournamentJoinResult.TournamentJoinResultType.TOURNAMENT_FULL) {
            return ResponseEntity.badRequest().build();
        }
        notificationService.notifyTournamentUpdate(new TournamentId(tournamentId));
        return ResponseEntity.ok(new JoinResponse(joinResult.playerId()));
    }

    @Operation(summary = "Plays a move",
            description = "Plays a move")
    @ApiResponse(responseCode = "200", description = "Successfully played the move")
    @ApiResponse(responseCode = "400", description = "Player is not part of the game or the move is invalid")
    @ApiResponse(responseCode = "404", description = "Game was not found")
    @PostMapping("/game/{gameId}/play")
    public ResponseEntity<Void> play(@PathVariable int gameId, @RequestBody Move move) {
        PlayResult playResult = gameService.play(move, new GameId(gameId));
        if (playResult.resultType() == PlayResultType.GAME_NOT_FOUND) {
            return ResponseEntity.notFound().build();
        }
        if (playResult.resultType() == PlayResultType.PLAYER_NOT_PART_OF_GAME || playResult.resultType() == PlayResultType.INVALID_ACTION) {
            return ResponseEntity.badRequest().build();
        }
        notificationService.notifyGameUpdate(new GameId(gameId));

        tournamentService.update();
        notificationService.notifyTournamentUpdate(new TournamentId(1));

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deletes a game",
            description = "Deletes a game")
    @ApiResponse(responseCode = "200", description = "Successfully deleted the game")
    @ApiResponse(responseCode = "404", description = "Game did not exist and can therefore not be deleted")
    @DeleteMapping("/game/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable int gameId) {
        boolean success = gameService.deleteGame(gameId);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deletes a tournament",
            description = "Deletes a tournament")
    @ApiResponse(responseCode = "200", description = "Successfully deleted the tournament")
    @ApiResponse(responseCode = "404", description = "Tournament did not exist and can therefore not be deleted")
    @DeleteMapping("/tournament/{tournamentId}")
    public ResponseEntity<Void> deleteTournament(@PathVariable int tournamentId) {
        boolean success = tournamentService.deleteTournament(tournamentId);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Starts a game",
            description = "Starts a game")
    @ApiResponse(responseCode = "200", description = "Successfully started the game")
    @ApiResponse(responseCode = "400", description = "Not enough players to start the game")
    @ApiResponse(responseCode = "404", description = "Game did not exist and can therefore not be started")
    @PostMapping("/game/{gameId}/start")
    public ResponseEntity<Void> startGame(@PathVariable int gameId) {
        StartResult result = gameService.startGame(gameId);
        if (result.resultType() == StartResult.StartResultType.GAME_NOT_FOUND) {
            return ResponseEntity.notFound().build();
        }
        if (result.resultType() == StartResult.StartResultType.NOT_ENOUGH_PLAYERS) {
            return ResponseEntity.badRequest().build();
        }
        notificationService.notifyGameUpdate(new GameId(gameId));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Starts a tournament",
            description = "Starts a tournament")
    @ApiResponse(responseCode = "200", description = "Successfully started the tournament")
    @ApiResponse(responseCode = "400", description = "Not enough players to start the tournament")
    @ApiResponse(responseCode = "404", description = "Tournament did not exist and can therefore not be started")
    @PostMapping("/tournament/{tournamentId}/start")
    public ResponseEntity<Void> startTournament(@PathVariable int tournamentId) {
        var result = tournamentService.startTournament(tournamentId);
        if (result.resultType() == TournamentStartResult.TournamentStartResultType.TOURNAMENT_NOT_FOUND) {
            return ResponseEntity.notFound().build();
        }
        if (result.resultType() == TournamentStartResult.TournamentStartResultType.NOT_ENOUGH_PLAYERS) {
            return ResponseEntity.badRequest().build();
        }
        notificationService.notifyTournamentUpdate(new TournamentId(tournamentId));
        return ResponseEntity.ok().build();
    }
}
