package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.BotDto;
import ch.zuehlke.common.GameId;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.common.TournamentId;
import ch.zuehlke.fullstack.hackathon.model.Game;
import ch.zuehlke.fullstack.hackathon.model.Tournament;
import ch.zuehlke.fullstack.hackathon.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final GameService gameService;
    private final TournamentService tournamentService;
    private final BotService botService;
    private final NotificationService notificationService;

    //TODO refactor login in ZTOPCHA-14
    @GetMapping("/login")
    public boolean login() {
        return adminService.login("Admin", "pass");
    }


    @Operation(summary = "Creates a new game",
            description = "Creates a new game and returns the game id")
    @ApiResponse(responseCode = "200", description = "Successfully created a new game")
    @PostMapping("/game")
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

    @ApiResponse(responseCode = "200", description = "Bot successfully generated")
    @ApiResponse(responseCode = "400", description = "Bot playerName invalid")
    @PostMapping("/bot/generate")
    public ResponseEntity<Void> generate(@RequestBody PlayerName name) {
        if (name == null || Objects.equals(name.value(), "") || name.value() == null || botService.getBot(name).orElse(null) != null)
            return ResponseEntity.badRequest().build();
        botService.addBot(name);
        return ResponseEntity.ok().build();
    }

    @ApiResponse(responseCode = "200", description = "Got bots successfully")
    @GetMapping("/bots")
    public ResponseEntity<List<BotDto>> getBots() {
        return ResponseEntity.ok(botService.getBots());
    }

    @ApiResponse(responseCode = "200", description = "Got bot successfully")
    @GetMapping("/bot/{name}")
    public ResponseEntity<BotDto> getBot(@PathVariable String name) {
        return ResponseEntity.ok(botService.getBot(new PlayerName(name)).orElse(null));
    }

    @ApiResponse(responseCode = "200", description = "Delete bot successfully")
    @ApiResponse(responseCode = "404", description = "Bot did not exist and can therefore not be deleted")
    @DeleteMapping("/bot/{name}")
    public ResponseEntity<Void> deleteBot(@PathVariable String name) {
        boolean success = botService.removeBot(new PlayerName(name));
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}