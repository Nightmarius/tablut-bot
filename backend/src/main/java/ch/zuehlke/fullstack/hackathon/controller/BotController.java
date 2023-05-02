package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.*;
import ch.zuehlke.fullstack.hackathon.service.BotAuthenticationService;
import ch.zuehlke.fullstack.hackathon.service.GameService;
import ch.zuehlke.fullstack.hackathon.service.NotificationService;
import ch.zuehlke.fullstack.hackathon.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class BotController {

    private final BotAuthenticationService botAuthService;
    private final GameService gameService;
    private final TournamentService tournamentService;
    private final NotificationService notificationService;

    @Operation(summary = "Joins a game",
            description = "Joins a game and returns the socket url")
    @ApiResponse(responseCode = "200", description = "Successfully joined the game")
    @ApiResponse(responseCode = "400", description = "Game is already full")
    @ApiResponse(responseCode = "403", description = "Bot credentials invalid")
    @ApiResponse(responseCode = "404", description = "The game does not exist")
    @PostMapping("/game/{gameId}/join")
    public ResponseEntity<JoinResponse> joinGame(@PathVariable int gameId, @RequestHeader String token, @RequestBody JoinRequest joinRequest) {
        /*BotDto bot = new BotDto(joinRequest.name(), new Token(token));
        AuthenticationResult result = botAuthService.authenticate(bot);
        if (result == AuthenticationResult.DENIED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }*/

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
    public ResponseEntity<JoinResponse> joinTournament(@PathVariable int tournamentId, @RequestHeader String token, @RequestBody JoinRequest joinRequest) {
        /*BotDto bot = new BotDto(joinRequest.name(), new Token(token));
        AuthenticationResult result = botAuthService.authenticate(bot);
        if (result == AuthenticationResult.DENIED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }*/

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
    public ResponseEntity<Void> play(@PathVariable int gameId, @RequestHeader String token, @RequestBody Move move) {
        //TODO get name here somehow
        /*PlayerName name = new PlayerName("bestBot");
        BotDto bot = new BotDto(name, new Token(token));
        AuthenticationResult result = botAuthService.authenticate(bot);
        if (result == AuthenticationResult.DENIED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }*/

        PlayResult playResult = gameService.play(move, new GameId(gameId));
        if (playResult == PlayResult.GAME_NOT_FOUND) {
            return ResponseEntity.notFound().build();
        }
        if (playResult == PlayResult.PLAYER_NOT_PART_OF_GAME || playResult == PlayResult.INVALID_ACTION) {
            return ResponseEntity.badRequest().build();
        }
        notificationService.notifyGameUpdate(new GameId(gameId));

        tournamentService.update();
        notificationService.notifyTournamentUpdate(new TournamentId(1));

        return ResponseEntity.ok().build();
    }
}